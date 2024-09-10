package co.edu.uniquindio.unieventos.services.implementacion;

import co.edu.uniquindio.unieventos.dto.compra.CrearCompraDTO;
import co.edu.uniquindio.unieventos.exceptions.CuponUsadoException;
import co.edu.uniquindio.unieventos.exceptions.EntradasInsuficientesException;
import co.edu.uniquindio.unieventos.exceptions.RecursoEncontradoException;
import co.edu.uniquindio.unieventos.exceptions.RecursoNoEncontradoException;
import co.edu.uniquindio.unieventos.model.*;
import co.edu.uniquindio.unieventos.repositories.CompraRepo;
import co.edu.uniquindio.unieventos.services.interfaces.CompraService;
import co.edu.uniquindio.unieventos.services.interfaces.CuponService;
import co.edu.uniquindio.unieventos.services.interfaces.EventoService;
import co.edu.uniquindio.unieventos.services.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CompraServiceImple implements CompraService {

    private final CompraRepo compraRepo;
    private final CuponService cuponService;
    private final UsuarioService usuarioService;
    private final EventoService eventoService;

    @Override
    public String crearCompra(CrearCompraDTO crearCompraDTO) throws Exception {

        /*
            VALIDACIÓN DEL CUPON
         */

        Cupon cupon = cuponService.obtenerCupon(crearCompraDTO.codigoCupon());

        if(cupon.getTipoCupon() == TipoCupon.UNICO){

            if(cupon.getEstadoCupon() == EstadoCupon.ACTIVO){

                cupon.setEstadoCupon(EstadoCupon.INACTIVO);

            }else{
                throw new CuponUsadoException("Este cupón ya fue usado por otra persona");
            }

        }else{

            Optional<Compra> compraOptional = compraRepo.findByCodigoCuponAndIdUsuario(crearCompraDTO.codigoCupon(), crearCompraDTO.idUsuario());

            if (compraOptional.isPresent()) {
                throw new RecursoEncontradoException("Este cupon ya ha sido redimido.");
            }

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
            Evento evento = eventoService.obtenerEvento(item.getIdEvento());

            // Buscar la localidad en el evento
            Localidad localidad = evento.getLocalidades().stream()
                    .filter(loc -> loc.getNombreLocalidad().equals(nombreLocalidad))
                    .findFirst()
                    .orElseThrow(() ->
                            new RecursoNoEncontradoException("Localidad no encontrada: " + nombreLocalidad + " en el evento: " + idEvento));

            // Verificar si hay suficientes entradas restantes
            if (localidad.getEntradasRestantes() < cantidad) {
                throw new EntradasInsuficientesException("No hay suficientes entradas restantes para la localidad: " + nombreLocalidad);
            }

            // Restar la cantidad de entradas
            localidad.setEntradasRestantes(localidad.getEntradasRestantes() - cantidad);

            //actulizar el evento
            eventoService.saveEvento(evento);
        }

        Usuario usuario = usuarioService.obtenerUsuario(crearCompraDTO.idUsuario());

        Compra compra = Compra.builder()
                .idUsuario(usuario.getId())
                .itemsCompra(crearCompraDTO.itemsCompra())
               // .total() //calcular el total acá
                .fechaCompra(LocalDateTime.now())
                .codigoCupon(crearCompraDTO.codigoCupon())
                .estado(EstadoCompra.PENDIENTE)
                .build();

        compraRepo.save(compra);

        return compra.getId();
    }

    @Override
    public Compra obtenerCompra(String idCompra) throws Exception {

        Optional<Compra> compraExistente = compraRepo.findById(idCompra);

        if (compraExistente.isEmpty()) {
            throw new RecursoNoEncontradoException("Compra no encontrada con el ID: " + idCompra);
        }

        return compraExistente.get();
    }

    @Override
    public List<Compra> obtenerComprasUsuario(String idUsuario){
        return compraRepo.findAllByIdUsuario(idUsuario);
    }

    @Override
    public String cancelarCompra(String idCompra) throws Exception {

        Compra compra = obtenerCompra(idCompra);

        compra.setEstado(EstadoCompra.CANCELADA);

        /*
            DEVOLVER LAS ENTRADAS COMPRADAS NUEVAMENTE A LA LOCALIDAD
         */

        for (ItemCompra item : compra.getItemsCompra()) {

            String idEvento = item.getIdEvento();
            String nombreLocalidad = item.getNombreLocalidad();
            Integer cantidad = item.getCantidad();

            // Buscar el evento correspondiente
            Evento evento = eventoService.obtenerEvento(item.getIdEvento());

            // Buscar la localidad en el evento
            Localidad localidad = evento.getLocalidades().stream()
                    .filter(loc -> loc.getNombreLocalidad().equals(nombreLocalidad))
                    .findFirst()
                    .orElseThrow(() ->
                            new Exception("Localidad no encontrada: " + nombreLocalidad + " en el evento: " + idEvento));

            // Devolver las entradas canceladas
            localidad.setEntradasRestantes(localidad.getEntradasRestantes() + cantidad);

            // Guardar el evento con las entradas actualizadas
            eventoService.saveEvento(evento);
        }

        compraRepo.save(compra);

        return "Compra cancelada exitosamente,";
    }

    @Override
    public String realizarCompra(String idCompra) {
        //TODO Llamar metodo de la pararela
        return "";
    }
}
