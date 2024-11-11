package co.edu.uniquindio.unieventos.services.implementacion;

import co.edu.uniquindio.unieventos.dto.carrito.*;
import co.edu.uniquindio.unieventos.exceptions.RecursoNoEncontradoException;
import co.edu.uniquindio.unieventos.model.*;
import co.edu.uniquindio.unieventos.repositories.CarritoRepo;
import co.edu.uniquindio.unieventos.services.interfaces.CarritoService;
import co.edu.uniquindio.unieventos.services.interfaces.EventoService;
import co.edu.uniquindio.unieventos.services.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CarritoServiceImple implements CarritoService {

    private final CarritoRepo carritoRepo;
    private final UsuarioService usuarioService;
    private final EventoService eventoService;

    @Override
    public String agregarAlCarrito(AgregarItemDTO agregarItemDTO) throws RecursoNoEncontradoException, IllegalArgumentException {
        // Obtener el carrito correspondiente
        Carrito carrito = obtenerCarrito(agregarItemDTO.idCarrito());

        // Obtener el evento asociado
        Evento evento = eventoService.obtenerEvento(agregarItemDTO.informacionDetalleCarritoDTO().idEvento());

        // Buscar la localidad seleccionada dentro del evento
        Optional<Localidad> localidadOptional = evento.getLocalidades().stream()
                .filter(localidad -> localidad.getNombreLocalidad().equals(agregarItemDTO.informacionDetalleCarritoDTO().nombreLocalidad()))
                .findFirst();

        // Si la localidad no existe, lanzar excepción
        if (localidadOptional.isEmpty()) {
            throw new RecursoNoEncontradoException("Localidad no encontrada");
        }

        Localidad localidad = localidadOptional.get();

        // Verificar si hay suficientes entradas disponibles
        if (localidad.getEntradasRestantes() < agregarItemDTO.informacionDetalleCarritoDTO().cantidad()) {
            throw new IllegalArgumentException("No hay suficientes entradas disponibles en la localidad seleccionada");
        }

        // Verificar si ya existe un item con la misma localidad en el carrito
        boolean existeItemEnCarrito = false;

        if(carrito.getItemsCarrito() != null){
            existeItemEnCarrito = carrito.getItemsCarrito().stream()
                    .anyMatch(item -> item.getNombreLocalidad().equals(agregarItemDTO.informacionDetalleCarritoDTO().nombreLocalidad())
                            && item.getEvento().getId().equals(agregarItemDTO.informacionDetalleCarritoDTO().idEvento()));
        }
        // Si ya existe, lanzar una excepción
        if (existeItemEnCarrito) {
            throw new IllegalArgumentException("Este item ya se encuentra en el carrito");
        }

        // Si no existe, agregar el item al carrito
        if (carrito.getItemsCarrito() != null) {
            carrito.getItemsCarrito().add(convertirDetalleCarritoDTO(agregarItemDTO.informacionDetalleCarritoDTO()));
        } else {
            carrito.setItemsCarrito(List.of(convertirDetalleCarritoDTO(agregarItemDTO.informacionDetalleCarritoDTO())));
        }

        // Guardar el carrito actualizado
        carritoRepo.save(carrito);

        return "Item agregado al carrito con éxito";
    }
    private DetalleCarrito convertirDetalleCarritoDTO(InformacionDetalleCarritoDTO informacionDetalleCarritoDTO) throws RecursoNoEncontradoException {
        Evento evento = eventoService.obtenerEvento(informacionDetalleCarritoDTO.idEvento());
        return DetalleCarrito.builder()
                .cantidad(informacionDetalleCarritoDTO.cantidad())
                .nombreLocalidad(informacionDetalleCarritoDTO.nombreLocalidad())
                .evento(evento)
                .build();
    }

    @Override
    public Carrito obtenerCarrito(String idCarrito) throws RecursoNoEncontradoException {
        if (idCarrito.length() != 24) {
            if (idCarrito.length() < 24) {
                // Si es más corto, completar con ceros al final
                idCarrito = String.format("%-24s", idCarrito).replace(' ', '0');
            } else {
                // Si es más largo, recortar a 24 caracteres
                idCarrito = idCarrito.substring(0, 24);
            }
        }
        Optional<Carrito> carritoExistente = carritoRepo.findById(idCarrito);

        if (carritoExistente.isEmpty()) {
            throw new RecursoNoEncontradoException("Carrito no encontrado");
        }

        return carritoExistente.get();
    }

    @Override
    public Carrito obtenerCarritoPorIdUsuario(String idUsuario) throws RecursoNoEncontradoException {
        // Validar la longitud del idUsuario para que tenga exactamente 24 caracteres
        if (idUsuario.length() != 24) {
            if (idUsuario.length() < 24) {
                // Si es más corto, completar con ceros al final
                idUsuario = String.format("%-24s", idUsuario).replace(' ', '0');
            } else {
                // Si es más largo, recortar a 24 caracteres
                idUsuario = idUsuario.substring(0, 24);
            }
        }
        // Buscar el carrito usando el ObjectId generado
        Optional<Carrito> carritoExistente = carritoRepo.findByIdUsuario(idUsuario);

        // Si no se encuentra el carrito, lanzar la excepción
        if (carritoExistente.isEmpty()) {
            throw new RecursoNoEncontradoException("Carrito no encontrado");
        }

        // Devolver el carrito encontrado
        return carritoExistente.get();
    }
    @Override
    public InformacionCarritoDTO obtenerCarritoPorIdUsuarioDTO(String idUsuario) throws RecursoNoEncontradoException {
        Carrito carrito = obtenerCarritoPorIdUsuario(idUsuario);
        List<InformacionDetalleCarritoDTO> informacionDetalleCarritoDTOS = convertirListaDellateCarritoDTO(carrito.getItemsCarrito());

        return new InformacionCarritoDTO(carrito.getId(), carrito.getFecha(), informacionDetalleCarritoDTOS, carrito.getUsuario().getId());
    }
    private List<InformacionDetalleCarritoDTO> convertirListaDellateCarritoDTO(List<DetalleCarrito> detalleCarritos) {
        if(detalleCarritos != null){
            return detalleCarritos.stream()
                    .map(detalleCarrito -> new InformacionDetalleCarritoDTO(
                            detalleCarrito.getCantidad(),
                            detalleCarrito.getNombreLocalidad(),
                            detalleCarrito.getEvento().getId()
                    ))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public String eliminarDelCarrito(EliminarDelCarritoDTO eliminarDelCarritoDTO) throws Exception {

        Carrito carrito = obtenerCarrito(eliminarDelCarritoDTO.idCarrito());
        List<DetalleCarrito> itemsFiltrados = null;
        if(carrito.getItemsCarrito() != null){
            itemsFiltrados = carrito.getItemsCarrito().stream()
                    .filter(item -> !(item.getNombreLocalidad().equals(eliminarDelCarritoDTO.nombreLocalidad()) &&
                            item.getEvento().getId().equals(eliminarDelCarritoDTO.idEvento())))
                    .collect(Collectors.toList());
        }

        carrito.setItemsCarrito(itemsFiltrados);

        // Dónde vuelve a aumentar la cantidad de entradas disponibles?

        carritoRepo.save(carrito);

        return "Item eliminado del carrito con éxito";

    }

    @Override
    public String editarCarrito(EditarCarritoDTO editarCarritoDTO) throws RecursoNoEncontradoException, IllegalArgumentException {

        // Obtener el carrito
        Carrito carrito = obtenerCarrito(editarCarritoDTO.idCarrito());
        Evento evento = eventoService.obtenerEvento(editarCarritoDTO.idEvento());

        // Buscar el item del carrito a editar
        Optional<DetalleCarrito> itemFiltrado = null;
        if(carrito.getItemsCarrito() != null){
            itemFiltrado = carrito.getItemsCarrito().stream()
                    .filter(detalleCarrito -> (detalleCarrito.getNombreLocalidad().equals(editarCarritoDTO.nombreLocalidad()) &&
                            detalleCarrito.getEvento().getId().equals(editarCarritoDTO.idEvento())))
                    .findFirst();
        }
        if (itemFiltrado.isEmpty()) {
            throw new RecursoNoEncontradoException("Item no encontrado en el carrito");
        } else {
            DetalleCarrito detalleCarrito = itemFiltrado.get();
            // Obtener la localidad para verificar entradas restantes
            Optional<Localidad> localidadOptional = evento.getLocalidades().stream()
                    .filter(localidad -> localidad.getNombreLocalidad().equals(editarCarritoDTO.nombreLocalidad()))
                    .findFirst();

            if (localidadOptional.isEmpty()) {
                throw new RecursoNoEncontradoException("Localidad no encontrada");
            }

            Localidad localidad = localidadOptional.get();

            // Verificar si hay suficientes entradas restantes
            if (localidad.getEntradasRestantes() < editarCarritoDTO.cantidadActualizada()) {
                throw new IllegalArgumentException("No hay suficientes entradas disponibles en la localidad seleccionada");
            }

            // Actualizar la cantidad en el carrito
            detalleCarrito.setCantidad(editarCarritoDTO.cantidadActualizada());

            // Guardar el carrito con los cambios
            carritoRepo.save(carrito);

            return "Carrito editado exitosamente";
        }
    }
    @Override
    public String crearCarrito(String idUsuario) throws Exception {

        Usuario usuario = usuarioService.obtenerUsuario(idUsuario);

        Carrito carrito = Carrito.builder()
                .fecha(LocalDateTime.now())
                .itemsCarrito(new ArrayList<>())
                .usuario(usuario)
                .build();

        carritoRepo.save(carrito);

        return "Carrito creado exitosamente";
    }

    @Override
    public String vaciarCarrito(String idUsuario) throws Exception {
        Carrito carrito = carritoRepo.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado para el usuario: " + idUsuario));

        carrito.setItemsCarrito(new ArrayList<>());
        carritoRepo.save(carrito);
        return "Carrito vaciado correctamente.";
    }

}
