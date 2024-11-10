package co.edu.uniquindio.unieventos.services.implementacion;

import co.edu.uniquindio.unieventos.dto.EmailDTO;
import co.edu.uniquindio.unieventos.dto.EnviarCuponCorreoDTO;
import co.edu.uniquindio.unieventos.dto.compra.CrearCompraDTO;
import co.edu.uniquindio.unieventos.dto.compra.InformacionCompraDTO;
import co.edu.uniquindio.unieventos.dto.compra.InformacionItemCompraDTO;
import co.edu.uniquindio.unieventos.dto.cupon.CrearCuponDTO;
import co.edu.uniquindio.unieventos.dto.cupon.InformacionCuponDTO;
import co.edu.uniquindio.unieventos.exceptions.EntradasInsuficientesException;
import co.edu.uniquindio.unieventos.exceptions.RecursoEncontradoException;
import co.edu.uniquindio.unieventos.exceptions.RecursoNoEncontradoException;
import co.edu.uniquindio.unieventos.model.*;
import co.edu.uniquindio.unieventos.repositories.CarritoRepo;
import co.edu.uniquindio.unieventos.repositories.CompraRepo;
import co.edu.uniquindio.unieventos.repositories.CuponRepo;
import co.edu.uniquindio.unieventos.repositories.UsuarioRepo;
import co.edu.uniquindio.unieventos.services.interfaces.*;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CompraServiceImple implements CompraService {

    private final CompraRepo compraRepo;
    private final CarritoRepo carritoRepo;
    private final CuponService cuponService;
    private final UsuarioService usuarioService;
    private final EventoService eventoService;
    private final EmailService emailService;
    // Cambiar cada que se vaya a probar la pasarela de pagos
    private final String urlNgrok = "https://53f0-2800-e2-7080-193b-81c0-8cd4-8767-407c.ngrok-free.app";
    private final CuponRepo cuponRepo;
    private final UsuarioRepo usuarioRepo;

    @Override
    public String crearCompra(CrearCompraDTO crearCompraDTO) throws Exception {

        /*
            VALIDACIÓN DEL CUPON
         */

        // Obtener el cupón por su código
        boolean isCupon = true;
        Cupon cupon = null;
        if(crearCompraDTO.codigoCupon() != null && !crearCompraDTO.codigoCupon().isEmpty()){
            cupon = cuponService.obtenerCuponPorCodigo(crearCompraDTO.codigoCupon());

            // Validar si el cupón está activo
            if (cupon.getEstadoCupon() != EstadoCupon.ACTIVO) {
                throw new IllegalArgumentException("El cupón no está disponible o ha expirado");
            }
            if(cupon.getFechaVencimiento().isBefore(LocalDate.now())){
                throw new IllegalArgumentException("El cupon ha expirado");
            }

            Optional<Compra> compraOptional = obtenerComprasPorCodigoCuponYIdUsuario(crearCompraDTO.codigoCupon(), crearCompraDTO.idUsuario());

            if (compraOptional.isPresent()) {
                throw new RecursoEncontradoException("Este cupón ya ha sido redimido por el usuario");
            }
        }else{
            isCupon = false;
        }

    /*
        VALIDACIÓN DE ENTRADAS Y ANTICIPACIÓN PARA LA LOCALIDAD
     */
        List<ItemCompra> itemsCompra = convertirListaItemCompraDTOToListaItemCompra(crearCompraDTO.informacionItemCompraDTOS());

        // Iterar sobre cada item de la compra
        for (ItemCompra item : itemsCompra) {
            String idEvento = item.getEvento().getId();
            String nombreLocalidad = item.getNombreLocalidad();
            Integer cantidad = item.getCantidad();

            // Buscar el evento correspondiente
            Evento evento = eventoService.obtenerEvento(idEvento);

            // Verificar si el evento ocurre con al menos dos días de anticipación
            if (evento.getFechaEvento().isBefore(LocalDateTime.now().plusDays(1))) {
                throw new IllegalArgumentException("Las compras deben realizarse con al menos dos días de anticipación.");
            }

            // Buscar la localidad en el evento
            Localidad localidad = evento.getLocalidades().stream()
                    .filter(loc -> loc.getNombreLocalidad().equals(nombreLocalidad))
                    .findFirst()
                    .orElseThrow(() ->
                            new RecursoNoEncontradoException("Localidad no encontrada"));

            // Verificar si hay suficientes entradas restantes
            if (localidad.getEntradasRestantes() < cantidad) {
                throw new EntradasInsuficientesException("No hay suficientes entradas restantes para la localidad");
            }
            localidad.setEntradasRestantes(localidad.getEntradasRestantes() - cantidad);

            // Actualizar el evento
            eventoService.saveEvento(evento);
        }

        Usuario usuario = usuarioService.obtenerUsuario(crearCompraDTO.idUsuario());

        double total = calcularTotal(itemsCompra);
        if (isCupon){
            total = calcularTotalConDescuentoCupon(cupon, total);
        }
        Compra compra = Compra.builder()
                .usuario(usuario)
                .itemsCompra(itemsCompra)
                .total(total)
                .fechaCompra(LocalDateTime.now())
                .codigoCupon(crearCompraDTO.codigoCupon())
                .estadoCompra(EstadoCompra.PENDIENTE)
                .build();

        compraRepo.save(compra);

        return compra.getId();
    }
    public InformacionCuponDTO validarYObtenerCupon(String codigoCupon, String idUsuario) throws RecursoNoEncontradoException {
        // Verificar que el código del cupón no sea nulo o vacío
        if (codigoCupon == null || codigoCupon.trim().isEmpty()) {
            throw new IllegalArgumentException("El código del cupón es inválido");
        }

        // Obtener el cupón por su código
        Cupon cupon = cuponService.obtenerCuponPorCodigo(codigoCupon);
        if (cupon == null) {
            throw new IllegalArgumentException("El cupón no existe");
        }

        // Validar si el cupón está activo
        if (cupon.getEstadoCupon() != EstadoCupon.ACTIVO) {
            throw new IllegalArgumentException("El cupón no está disponible o ha expirado");
        }

        // Validar si el cupón ha expirado
        if (cupon.getFechaVencimiento().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("El cupón ha expirado");
        }

        Optional<Compra> compraOptional = obtenerComprasPorCodigoCuponYIdUsuario(codigoCupon, idUsuario);
        if (compraOptional.isPresent()) {
            throw new IllegalArgumentException("Este cupón ya ha sido redimido por el usuario");
        }

        // Si todas las validaciones pasan, devolvemos el cupón
        return new InformacionCuponDTO(cupon.getCodigo(), cupon.getPorcentajeDescuento());
    }

    private List<ItemCompra> convertirListaItemCompraDTOToListaItemCompra(List<InformacionItemCompraDTO> informacionItemCompraDTOList) throws RecursoNoEncontradoException {
        List<ItemCompra> itemCompraList = new ArrayList<>();
        for (InformacionItemCompraDTO informacionItemCompraDTO : informacionItemCompraDTOList){
            Evento evento = eventoService.obtenerEvento(informacionItemCompraDTO.idEvento());
            ItemCompra itemCompra = ItemCompra.builder()
                    .evento(evento)
                    .nombreLocalidad(informacionItemCompraDTO.nombreLocalidad())
                    .cantidad(informacionItemCompraDTO.cantidad())
                    .precioUnitario(informacionItemCompraDTO.precioUnitario())
                    .build();
            itemCompraList.add(itemCompra);
        }
        return itemCompraList;
    }
    private List<InformacionItemCompraDTO> convertirListaItemCompraToListaItemCompraDTO(List<ItemCompra> informacionItemCompraList) {
        List<InformacionItemCompraDTO> informacionitemCompraDtoList = new ArrayList<>();
        for (ItemCompra itemCompra : informacionItemCompraList){
            InformacionItemCompraDTO informacionItemCompraDTO = new InformacionItemCompraDTO(
                    itemCompra.getEvento().getId(),
                    itemCompra.getNombreLocalidad(),
                    itemCompra.getCantidad(),
                    itemCompra.getPrecioUnitario()
            );
            informacionitemCompraDtoList.add(informacionItemCompraDTO);
        }
        return informacionitemCompraDtoList;
    }

    private double calcularTotalConDescuentoCupon(Cupon cupon, double total) {
        return total - total * (cupon.getPorcentajeDescuento() / 100);
    }
    private double calcularTotal(List<ItemCompra> itemsCompra) {
        return itemsCompra.stream().mapToDouble((item) -> item.getPrecioUnitario() * item.getCantidad()).sum();
    }

    @Override
    public Compra obtenerCompra(String idCompra) throws Exception {
        if (idCompra.length() != 24) {
            if (idCompra.length() < 24) {
                // Si es más corto, completar con ceros al final
                idCompra = String.format("%-24s", idCompra).replace(' ', '0');
            } else {
                // Si es más largo, recortar a 24 caracteres
                idCompra = idCompra.substring(0, 24);
            }
        }
        Optional<Compra> compraExistente = compraRepo.findById(idCompra);

        if (compraExistente.isEmpty()) {
            throw new RecursoNoEncontradoException("Compra no encontrada");
        }

        return compraExistente.get();
    }
    @Override
    public String obtenerEstadoCompra(String idCompra) throws Exception {
        Compra compraEncontrada = obtenerCompra(idCompra);

        return "" + compraEncontrada.getEstadoCompra();
    }
    @Override
    public InformacionCompraDTO obtenerCompraDTO(String idCompra) throws Exception {

        Compra compra = obtenerCompra(idCompra);
        List<InformacionItemCompraDTO> informacionItemCompraDTO = convertirListaItemCompraToListaItemCompraDTO(compra.getItemsCompra());
        return new InformacionCompraDTO(
                compra.getId(),
                compra.getUsuario().getId(),
                informacionItemCompraDTO,
                compra.getTotal(),
                compra.getFechaCompra(),
                compra.getCodigoCupon(),
                compra.getEstadoCompra(),
                compra.getCodigoPasarela(),
                compra.getPago()
        );
    }
    @Override
    public List<Compra> obtenerComprasUsuario(String idUsuario){
        if (idUsuario.length() != 24) {
            if (idUsuario.length() < 24) {
                // Si es más corto, completar con ceros al final
                idUsuario = String.format("%-24s", idUsuario).replace(' ', '0');
            } else {
                // Si es más largo, recortar a 24 caracteres
                idUsuario = idUsuario.substring(0, 24);
            }
        }
        return compraRepo.findAllByIdUsuarioAndEstadoNoy(idUsuario, EstadoCompra.PENDIENTE);
    }
    private List<Compra> obtenerTodasLasComprasUsuario(String idUsuario){
        if (idUsuario.length() != 24) {
            if (idUsuario.length() < 24) {
                // Si es más corto, completar con ceros al final
                idUsuario = String.format("%-24s", idUsuario).replace(' ', '0');
            } else {
                // Si es más largo, recortar a 24 caracteres
                idUsuario = idUsuario.substring(0, 24);
            }
        }
        return compraRepo.findAllByIdUsuario(idUsuario);
    }
    @Override
    public List<InformacionCompraDTO> obtenerComprasUsuarioDTO(String idUsuario) {
        List<InformacionCompraDTO> informacionCompraDTOList = new ArrayList<>();
        List<Compra> comprasUsuario = obtenerComprasUsuario(idUsuario);
        InformacionCompraDTO informacionCompraDTO = null;
        for (Compra compra : comprasUsuario){
            List<InformacionItemCompraDTO> informacionItemCompraDTOList = convertirListaItemCompraToListaItemCompraDTO(compra.getItemsCompra());
            informacionCompraDTO = new InformacionCompraDTO(
                    compra.getId(),
                    compra.getUsuario().getId(),
                    informacionItemCompraDTOList,
                    compra.getTotal(),
                    compra.getFechaCompra(),
                    compra.getCodigoCupon(),
                    compra.getEstadoCompra(),
                    compra.getCodigoPasarela(),
                    compra.getPago()
            );
            informacionCompraDTOList.add(informacionCompraDTO);
        }
        return informacionCompraDTOList;
    }

    @Override
    public String cancelarCompra(String idCompra) throws Exception {

        Compra compra = obtenerCompra(idCompra);
        if(compra.getEstadoCompra() != EstadoCompra.PENDIENTE){
            throw new IllegalArgumentException("La compra debe estar PENDIENTE");
        }

        /*
            DEVOLVER LAS ENTRADAS COMPRADAS NUEVAMENTE A LA LOCALIDAD
         */

        for (ItemCompra item : compra.getItemsCompra()) {

            String idEvento = item.getEvento().getId();
            String nombreLocalidad = item.getNombreLocalidad();
            Integer cantidad = item.getCantidad();

            // Buscar el evento correspondiente
            Evento evento = eventoService.obtenerEvento(item.getEvento().getId());

            if(evento.getLocalidades() != null){
                // Buscar la localidad en el evento
                Localidad localidad = evento.getLocalidades().stream()
                        .filter(loc -> loc.getNombreLocalidad().equals(nombreLocalidad))
                        .findFirst()
                        .orElseThrow(() ->
                                new Exception("Localidad no encontrada"));

                // Devolver las entradas canceladas
                localidad.setEntradasRestantes(localidad.getEntradasRestantes() + cantidad);

                // Guardar el evento con las entradas actualizadas
                eventoService.saveEvento(evento);
            }
        }
        compra.setEstadoCompra(EstadoCompra.CANCELADA);
        compraRepo.save(compra);

        return "Compra cancelada exitosamente";
    }

    @Override
    public Preference realizarPago(String idCompra) throws Exception {


        // Obtener la orden guardada en la base de datos y los ítems de la orden
        Compra compraGuardada = obtenerCompra(idCompra);
        Cupon cupon = null;
        if(compraGuardada.getCodigoCupon() != null && !compraGuardada.getCodigoCupon().isEmpty()){
            cupon = cuponService.obtenerCuponPorCodigo(compraGuardada.getCodigoCupon());
            if(cupon.getEstadoCupon() == EstadoCupon.ACTIVO){
                if(cupon.getTipoCupon() == TipoCupon.UNICO){
                    cupon.setEstadoCupon(EstadoCupon.INACTIVO);
                    cuponRepo.save(cupon);
                }
            }else{
                cupon = null;
            }
        }


        List<PreferenceItemRequest> itemsPasarela = new ArrayList<>();


        // Recorrer los items de la orden y crea los ítems de la pasarela
        for(ItemCompra item : compraGuardada.getItemsCompra()){


            // Obtener el evento y la localidad del ítem
            Evento evento = eventoService.obtenerEvento(item.getEvento().getId());
            Localidad localidad = evento.getLocalidades().stream()
                    .filter(localidad1 -> localidad1.getNombreLocalidad().equals(item.getNombreLocalidad()))
                    .findFirst()
                    .orElse(null);

            double precioLocalidad = localidad.getPrecioLocalidad();
            if(cupon != null){
                precioLocalidad = calcularPrecioDescuentoCupon(cupon, precioLocalidad);
            }

            // Crear el item de la pasarela
            PreferenceItemRequest itemRequest =
                    PreferenceItemRequest.builder()
                            .id(evento.getId())
                            .title(evento.getNombreEvento())
                            .pictureUrl(evento.getImagenPortada())
                            .categoryId(evento.getTipoEvento().name())
                            .quantity(item.getCantidad())
                            .currencyId("COP")
                            .unitPrice(BigDecimal.valueOf(precioLocalidad))
                            .build();


            itemsPasarela.add(itemRequest);
        }


        // Configurar las credenciales de MercadoPago
        MercadoPagoConfig.setAccessToken("APP_USR-1683778910738309-100810-244312086b9b60a77c052359c31c2376-2024325571");


        // Configurar las urls de retorno de la pasarela (Frontend)
        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success("http://localhost:4200/detalle-compra/" + idCompra)
                .failure("http://localhost:4200/detalle-compra/" + idCompra)
                .pending("http://localhost:4200/detalle-compra/" + idCompra)
                .build();


        // Construir la preferencia de la pasarela con los ítems, metadatos y urls de retorno
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .backUrls(backUrls)
                .items(itemsPasarela)
                .metadata(Map.of("id_orden", compraGuardada.getId()))
                .notificationUrl(urlNgrok + "/api/notificaciones/mercadopago")
                .build();


        // Crear la preferencia en la pasarela de MercadoPago
        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest);


        // Guardar el código de la pasarela en la orden
        compraGuardada.setCodigoPasarela( preference.getId() );
        compraRepo.save(compraGuardada);

        Optional<Carrito> carritoOptional = carritoRepo.findByIdUsuario(compraGuardada.getUsuario().getId());

        if (carritoOptional.isEmpty()) {
            throw new RecursoNoEncontradoException("Carrito no encontrado con el id del usuario: " + compraGuardada.getUsuario().getId());
        }

        Carrito carrito = carritoOptional.get();

        List<DetalleCarrito> detalleCarritos = new ArrayList<>();

        carrito.setItemsCarrito(detalleCarritos);

        carritoRepo.save(carrito);

        // Metodo que envia el correo con el cupon de primera compra
        Optional<Usuario> usuarioOptional = Optional.ofNullable(usuarioService.obtenerUsuario(compraGuardada.getUsuario().getId()));
        Usuario usuario = usuarioOptional.get();

        if (!usuario.getPrimeraCompraRealizada()){
            String codigoCupon = cuponService.generarCodigoCupon();
            CrearCuponDTO cuponDTO = new CrearCuponDTO(codigoCupon, "CUPONPRIMERACOMPRA", 25.0, TipoCupon.UNICO, LocalDate.now().plusDays(30), usuario);
            cuponService.crearCupon(cuponDTO);
            usuario.getCuponesUsuario().add(cuponService.obtenerCuponPorCodigo(cuponDTO.codigo()));
            usuario.setPrimeraCompraRealizada(true);
            usuarioRepo.save(usuario);

            EnviarCuponCorreoDTO enviarCuponCorreoDTO = new EnviarCuponCorreoDTO(
                    "Tu nuevo cupon",
                    "Felicidades, por realizar tu primera compra obtienes " + cuponDTO.porcentajeDescuento()
                            + "% de descuento en tu próxima compra",
                    cuponDTO.nombre(),
                    cuponDTO.codigo()
            );
            emailService.enviarCuponCorreo(usuario.getEmail(), enviarCuponCorreoDTO);
        }

        // Obtiene las compra que realizó el usuario, recorre cada compra individual y sus
        // localidades, verifica que se puedan restar (por si alguien más las compró antes)
        // y resta las entradas compradas a las entradas restantes
        if (compraGuardada.getEstadoCompra() == EstadoCompra.COMPLETADA){
            List<ItemCompra> itemsCompra = compraGuardada.getItemsCompra();

            // Iterar sobre cada item de la compra
            for (ItemCompra item : itemsCompra) {

                String idEvento = item.getEvento().getId();
                String nombreLocalidad = item.getNombreLocalidad();
                Integer cantidad = item.getCantidad();

                // Buscar el evento correspondiente
                Evento evento = eventoService.obtenerEvento(idEvento);

                // Buscar la localidad en el evento
                Localidad localidad = evento.getLocalidades().stream()
                        .filter(loc -> loc.getNombreLocalidad().equals(nombreLocalidad))
                        .findFirst()
                        .orElseThrow(() ->
                                new RecursoNoEncontradoException("Localidad no encontrada"));

                // Verificar si hay suficientes entradas restantes
                if (localidad.getEntradasRestantes() < cantidad) {
                    throw new EntradasInsuficientesException("No hay suficientes entradas restantes para la localidad");
                }

                // Resta las entradas compradas a las entradas restantes
                localidad.setEntradasRestantes(localidad.getEntradasRestantes() - cantidad);

                //actulizar el evento
                eventoService.saveEvento(evento);
            }
        }

        return preference;
    }
    private double calcularPrecioDescuentoCupon(Cupon cupon, double precio) {
        return precio - (precio*cupon.getPorcentajeDescuento() / 100);
    }

    private Optional<Compra> obtenerComprasPorCodigoCuponYIdUsuario(String codigoCupon, String idUsuario) {
        if (idUsuario.length() != 24) {
            if (idUsuario.length() < 24) {
                // Si es más corto, completar con ceros al final
                idUsuario = String.format("%-24s", idUsuario).replace(' ', '0');
            } else {
                // Si es más largo, recortar a 24 caracteres
                idUsuario = idUsuario.substring(0, 24);
            }
        }
        return compraRepo.findByCodigoCuponAndIdUsuario(codigoCupon, idUsuario);
    }

    private Pago crearPago(Payment payment) {
        Pago pago = new Pago();
        pago.setId(payment.getId().toString());
        pago.setFechaPago( payment.getDateCreated().toLocalDateTime() );
        pago.setEstado(payment.getStatus());
        pago.setDetalleEstado(payment.getStatusDetail());
        pago.setTipoPago(payment.getPaymentTypeId());
        pago.setMoneda(payment.getCurrencyId());
        pago.setCodigoAutorizacion(payment.getAuthorizationCode());
        pago.setValorTransaccion(payment.getTransactionAmount().doubleValue());
        return pago;
    }

    private byte[] generateQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }
    @Override
    public String procesarNotificacionDePago(Map<String, Object> request) {
        try {
            MercadoPagoConfig.setAccessToken("APP_USR-1683778910738309-100810-244312086b9b60a77c052359c31c2376-2024325571");
            // Obtener el tipo de notificación
            Object tipo = request.get("type");

            // Si la notificación es de un pago, obtenemos el pago y la orden asociada
            if ("payment".equals(tipo)) {
                // Capturamos el JSON que viene en el request y lo convertimos a String
                String input = request.get("data").toString();

                // Extraemos los números de la cadena, es decir, el id del pago
                String idPago = input.replaceAll("\\D+", "");

                // Se crea el cliente de MercadoPago y se obtiene el pago con el id
                PaymentClient paymentClient = new PaymentClient();
                Payment pago = paymentClient.get(Long.parseLong(idPago)); // Obtener el pago usando el paymentId

                // Obtener el id de la orden asociada al pago que viene en los metadatos
                String idOrden = pago.getMetadata().get("id_orden").toString(); // Obtener el id_orden de los metadatos

                // Verifica el estado del pago
                String estadoPago = pago.getStatus(); // Obtiene el estado del pago: approved, pending, rejected, etc.

                // Busca la compra con el id_orden
                Compra compra = obtenerCompra(idOrden); // Buscar la compra por el id_orden
                if (compra != null) {
                    // Actualiza el estado de la compra dependiendo del estado del pago
                    switch (estadoPago.toLowerCase()) {
                        case "approved":
                            compra.setEstadoCompra(EstadoCompra.COMPLETADA); // Si el pago es aprobado, marca la compra como completada
                            break;
                        case "pending":
                            compra.setEstadoCompra(EstadoCompra.PENDIENTE); // Si el pago está pendiente, marca la compra como pendiente
                            break;
                        case "rejected":
                            compra.setEstadoCompra(EstadoCompra.RECHAZADA); // Si el pago fue rechazado, marca la compra como rechazada
                            break;
                        case "cancelled":
                            compra.setEstadoCompra(EstadoCompra.CANCELADA); // Si el pago fue cancelado, marca la compra como cancelada
                            break;
                        default:
                            compra.setEstadoCompra(EstadoCompra.PENDIENTE); // Por defecto, si el estado es desconocido, lo marcamos como pendiente
                            break;
                    }

                    // Crea el objeto Pago y asocia a la compra
                    Pago pagoBackend = crearPago(pago); // Crear el pago en el backend (almacena detalles del pago)
                    compra.setPago(pagoBackend); // Asocia el pago con la compra

                    // Guarda la compra con el nuevo estado
                    compraRepo.save(compra);

                    // Si el pago fue aprobado, se crea un código QR
                    if ("approved".equalsIgnoreCase(estadoPago)) {
                        // Se crea un código QR con el código de la compra por medio de un servicio externo
                        byte[] qrCode = generateQRCodeImage(compra.getId(), 350, 350);

                        // Crear el contenido del correo con el código QR embebido
                        String emailContent = "<html><body>" +
                                "Tu compra ha sido completada con éxito. Aquí está tu código QR:<br>" +
                                "<img src='cid:qrCode' />" + // `cid` se refiere al contenido embebido adjuntado
                                "</body></html>";

                        // Enviar el correo con el código QR embebido
                        emailService.enviarCorreoSimple(compra.getUsuario().getEmail(), "Confirmación de compra", emailContent, qrCode);
                    }

                    return "Notificación procesada y compra actualizada con éxito";
                } else {
                    return "Compra no encontrada con el id_orden proporcionado";
                }
            } else {
                return "Tipo de notificación no soportado";
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar la notificación de pago: " + e.getMessage());
        }
    }
}
