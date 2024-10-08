package co.edu.uniquindio.unieventos.services.implementacion;

import co.edu.uniquindio.unieventos.dto.carrito.AgregarItemDTO;
import co.edu.uniquindio.unieventos.dto.carrito.EditarCarritoDTO;
import co.edu.uniquindio.unieventos.dto.carrito.EliminarDelCarritoDTO;
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

        Carrito carrito = obtenerCarrito(agregarItemDTO.idCarrito());
        Evento evento = eventoService.obtenerEvento(agregarItemDTO.detalleCarrito().getIdEvento());
        Optional<Localidad> localidadOptional = evento.getLocalidades().stream()
                .filter(localidad -> localidad.getNombreLocalidad().equals(agregarItemDTO.detalleCarrito().getNombreLocalidad()))
                .findFirst();

        if (localidadOptional.isEmpty()) {
            throw new RecursoNoEncontradoException("Localidad no encontrada");
        }

        Localidad localidad = localidadOptional.get();

        // Verificar si hay suficientes entradas restantes
        if (localidad.getEntradasRestantes() < agregarItemDTO.detalleCarrito().getCantidad()) {
            throw new IllegalArgumentException("No hay suficientes entradas disponibles en la localidad seleccionada");
        }

        carrito.getItemsCarrito().add(agregarItemDTO.detalleCarrito());
        carritoRepo.save(carrito);

        return "Item agregado al carrito con éxito";
    }

    @Override
    public Carrito obtenerCarrito(String idCarrito) throws RecursoNoEncontradoException {

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
    public String eliminarDelCarrito(EliminarDelCarritoDTO eliminarDelCarritoDTO) throws Exception {

        Carrito carrito = obtenerCarrito(eliminarDelCarritoDTO.idCarrito());

        List<DetalleCarrito> itemsFiltrados = carrito.getItemsCarrito().stream()
                .filter(item -> !(item.getNombreLocalidad().equals(eliminarDelCarritoDTO.nombreLocalidad()) &&
                        item.getIdEvento().equals(eliminarDelCarritoDTO.idEvento())))
                .collect(Collectors.toList());

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
        Optional<DetalleCarrito> itemFiltrado = carrito.getItemsCarrito().stream()
                .filter(detalleCarrito -> (detalleCarrito.getNombreLocalidad().equals(editarCarritoDTO.nombreLocalidad()) &&
                        detalleCarrito.getIdEvento().equals(editarCarritoDTO.idEvento())))
                .findFirst();

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
                .idUsuario(usuario.getId())
                .build();

        carritoRepo.save(carrito);

        return "Carrito creado exitosamente";
    }


}
