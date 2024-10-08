package co.edu.uniquindio.unieventos.services.implementacion;

import co.edu.uniquindio.unieventos.dto.compra.CrearCompraDTO;
import co.edu.uniquindio.unieventos.exceptions.EntradasInsuficientesException;
import co.edu.uniquindio.unieventos.exceptions.RecursoEncontradoException;
import co.edu.uniquindio.unieventos.exceptions.RecursoNoEncontradoException;
import co.edu.uniquindio.unieventos.model.*;
import co.edu.uniquindio.unieventos.repositories.CarritoRepo;
import co.edu.uniquindio.unieventos.repositories.CompraRepo;
import co.edu.uniquindio.unieventos.repositories.CuponRepo;
import co.edu.uniquindio.unieventos.services.interfaces.CompraService;
import co.edu.uniquindio.unieventos.services.interfaces.CuponService;
import co.edu.uniquindio.unieventos.services.interfaces.EventoService;
import co.edu.uniquindio.unieventos.services.interfaces.UsuarioService;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CompraServiceImple implements CompraService {

    private final CompraRepo compraRepo;
    private final CarritoRepo carritoRepo;
    private final CuponService cuponService;
    private final UsuarioService usuarioService;
    private final EventoService eventoService;
    // Cambiar cada que se vaya a probar la pasarela de pagos
    private final String urlNgrok = "https://01f8-181-53-99-0.ngrok-free.app/";
    private final CuponRepo cuponRepo;

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
                cupon.setEstadoCupon(EstadoCupon.INACTIVO);
                cuponRepo.save(cupon);
                throw new IllegalArgumentException("El cupon ha expirado");
            }

            // Validación para cupón único
            if (cupon.getTipoCupon() == TipoCupon.UNICO) {
                // Cambiar el estado del cupón a inactivo una vez utilizado
                cupon.setEstadoCupon(EstadoCupon.INACTIVO);

            } else {
                // Validación para cupones generales (múltiples)
                Optional<Compra> compraOptional = obtenerComprasPorCodigoCuponYIdUsuario(crearCompraDTO.codigoCupon(), crearCompraDTO.idUsuario());

                if (compraOptional.isPresent()) {
                    throw new RecursoEncontradoException("Este cupón ya ha sido redimido por el usuario");
                }
            }
        }else{
            isCupon = false;
        }

        /*
            VALIDACIÓN DE ENTRADAS PARA LA LOCALIDAD
         */

        List<ItemCompra> itemsCompra = crearCompraDTO.itemsCompra();

        // Iterar sobre cada item de la compra
        for (ItemCompra item : itemsCompra) {

            String idEvento = item.getIdEvento();
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

            // Restar la cantidad de entradas
            localidad.setEntradasRestantes(localidad.getEntradasRestantes() - cantidad);

            //actulizar el evento
            eventoService.saveEvento(evento);
        }

        Usuario usuario = usuarioService.obtenerUsuario(crearCompraDTO.idUsuario());

        double total = calcularTotal(itemsCompra);
        if (isCupon){
            total = calcularTotalConDescuentoCupon(cupon, total);
        }
        Compra compra = Compra.builder()
                .idUsuario(usuario.getId())
                .itemsCompra(crearCompraDTO.itemsCompra())
                .total(total)
                .fechaCompra(LocalDateTime.now())
                .codigoCupon(crearCompraDTO.codigoCupon())
                .estadoCompra(EstadoCompra.PENDIENTE)
                .build();

        compraRepo.save(compra);

        return compra.getId();
    }

    private double calcularTotalConDescuentoCupon(Cupon cupon, double total) {
        return total - total * (cupon.getPorcentajeDescuento() / 100);
    }
    private double calcularTotal(List<ItemCompra> itemsCompra) {
        return itemsCompra.stream().mapToDouble((item) -> item.getPrecioUnitario() * item.getCantidad()).sum();
    }

    @Override
    public Compra obtenerCompra(String idCompra) throws Exception {

        Optional<Compra> compraExistente = compraRepo.findById(idCompra);

        if (compraExistente.isEmpty()) {
            throw new RecursoNoEncontradoException("Compra no encontrada");
        }

        return compraExistente.get();
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
        return compraRepo.findAllByIdUsuario(new ObjectId(idUsuario));
    }

    @Override
    public String cancelarCompra(String idCompra) throws Exception {

        Compra compra = obtenerCompra(idCompra);
        if(compra.getEstadoCompra() != EstadoCompra.PENDIENTE){
            throw new IllegalArgumentException("La compra debe estar PENDIENTE");
        }

        compra.setEstadoCompra(EstadoCompra.CANCELADA);

        /*
            DEVOLVER LAS ENTRADAS COMPRADAS NUEVAMENTE A LA LOCALIDAD
         */

        for (ItemCompra item : compra.getItemsCompra()) {

            String idEvento = item.getIdEvento();
            String nombreLocalidad = item.getNombreLocalidad();
            Integer cantidad = item.getCantidad();

            // Buscar el evento correspondiente
            Evento evento = eventoService.obtenerEvento(item.getIdEvento());

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

        compraRepo.save(compra);

        return "Compra cancelada exitosamente";
    }

    @Override
    public Preference realizarPago(String idCompra) throws Exception {


        // Obtener la orden guardada en la base de datos y los ítems de la orden
        Compra compraGuardada = obtenerCompra(idCompra);
        List<PreferenceItemRequest> itemsPasarela = new ArrayList<>();


        // Recorrer los items de la orden y crea los ítems de la pasarela
        for(ItemCompra item : compraGuardada.getItemsCompra()){


            // Obtener el evento y la localidad del ítem
            Evento evento = eventoService.obtenerEvento(item.getIdEvento());
            Localidad localidad = evento.getLocalidades().stream()
                    .filter(localidad1 -> localidad1.getNombreLocalidad().equals(item.getNombreLocalidad()))
                    .findFirst()
                    .orElse(null);

            // Crear el item de la pasarela
            PreferenceItemRequest itemRequest =
                    PreferenceItemRequest.builder()
                            .id(evento.getId())
                            .title(evento.getNombreEvento())
                            .pictureUrl(evento.getImagenPortada())
                            .categoryId(evento.getTipoEvento().name())
                            .quantity(item.getCantidad())
                            .currencyId("COP")
                            .unitPrice(BigDecimal.valueOf(localidad.getPrecioLocalidad()))
                            .build();


            itemsPasarela.add(itemRequest);
        }


        // Configurar las credenciales de MercadoPago
        MercadoPagoConfig.setAccessToken("APP_USR-1683778910738309-100810-244312086b9b60a77c052359c31c2376-2024325571");


        // Configurar las urls de retorno de la pasarela (Frontend)
        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success(urlNgrok + "compra/success")
                .failure(urlNgrok + "compra/failure")
                .pending(urlNgrok + "compra/pending")
                .build();


        // Construir la preferencia de la pasarela con los ítems, metadatos y urls de retorno
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .backUrls(backUrls)
                .items(itemsPasarela)
                .metadata(Map.of("id_orden", compraGuardada.getId()))
                .notificationUrl(urlNgrok)
                .build();


        // Crear la preferencia en la pasarela de MercadoPago
        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest);


        // Guardar el código de la pasarela en la orden
        compraGuardada.setCodigoPasarela( preference.getId() );
        compraRepo.save(compraGuardada);

        Optional<Carrito> carritoOptional = carritoRepo.findByIdUsuario(compraGuardada.getIdUsuario());

        if (carritoOptional.isEmpty()) {
            throw new RecursoNoEncontradoException("Carrito no encontrado con el id del usuario: " + compraGuardada.getIdUsuario());
        }

        Carrito carrito = carritoOptional.get();

        List<DetalleCarrito> detalleCarritos = new ArrayList<>();

        carrito.setItemsCarrito(detalleCarritos);

        carritoRepo.save(carrito);

        return preference;
    }

    @Override
    public void recibirNotificacionMercadoPago(Map<String, Object> request) {
        try {


            // Obtener el tipo de notificación
            Object tipo = request.get("type");


            // Si la notificación es de un pago entonces obtener el pago y la orden asociada
            if ("payment".equals(tipo)) {


                // Capturamos el JSON que viene en el request y lo convertimos a un String
                String input = request.get("data").toString();


                // Extraemos los números de la cadena, es decir, el id del pago
                String idPago = input.replaceAll("\\D+", "");


                // Se crea el cliente de MercadoPago y se obtiene el pago con el id
                PaymentClient client = new PaymentClient();
                Payment payment = client.get( Long.parseLong(idPago) );


                // Obtener el id de la orden asociada al pago que viene en los metadatos
                String idCompra = payment.getMetadata().get("id_orden").toString();


                // Se obtiene la orden guardada en la base de datos y se le asigna el pago
                Compra compra = obtenerCompra(idCompra);
                Pago pago = crearPago(payment);
                compra.setPago(pago);
                compraRepo.save(compra);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
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



}
