package co.edu.uniquindio.unieventos.services.implementacion;

import co.edu.uniquindio.unieventos.dto.carrito.AgregarItemDTO;
import co.edu.uniquindio.unieventos.dto.carrito.EditarCarritoDTO;
import co.edu.uniquindio.unieventos.dto.carrito.EliminarDelCarritoDTO;
import co.edu.uniquindio.unieventos.exceptions.RecursoNoEncontradoException;
import co.edu.uniquindio.unieventos.model.Carrito;
import co.edu.uniquindio.unieventos.model.DetalleCarrito;
import co.edu.uniquindio.unieventos.model.Evento;
import co.edu.uniquindio.unieventos.model.Usuario;
import co.edu.uniquindio.unieventos.repositories.CarritoRepo;
import co.edu.uniquindio.unieventos.services.interfaces.CarritoService;
import co.edu.uniquindio.unieventos.services.interfaces.EventoService;
import co.edu.uniquindio.unieventos.services.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
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
    public String agregarAlCarrito(AgregarItemDTO agregarItemDTO) throws Exception {

        Carrito carrito = obtenerCarrito(agregarItemDTO.idCarrito());

        carrito.getItemsCarrito().add(agregarItemDTO.detalleCarrito());

        carritoRepo.save(carrito);

        Evento evento = eventoService.obtenerEvento(agregarItemDTO.detalleCarrito().getIdEvento());

        evento.getLocalidades().stream()
                .filter(localidad -> localidad.getNombreLocalidad().equals(agregarItemDTO.detalleCarrito().getNombreLocalidad()))
                .findFirst()
                .ifPresent(localidad -> {
                    localidad.setEntradasRestantes(localidad.getEntradasRestantes() - agregarItemDTO.detalleCarrito().getCantidad());
                    try {
                        eventoService.saveEvento(evento);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });


        return "Item agregado al carrito con éxito.";
    }

    @Override
    public Carrito obtenerCarrito(String idCarrito) throws Exception {

        Optional<Carrito> carritoExistente = carritoRepo.findById(idCarrito);

        if (carritoExistente.isEmpty()) {
            throw new RecursoNoEncontradoException("Carrito no encontrado con el ID: " + idCarrito);
        }

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

        return "Item eliminado del carrito con éxito.";

    }

    @Override
    public String editarCarrito(EditarCarritoDTO editarCarritoDTO) throws Exception {

        Carrito carrito = obtenerCarrito(editarCarritoDTO.idCarrito());

        Optional<DetalleCarrito> itemFiltrado = carrito.getItemsCarrito().stream()
                .filter(detalleCarrito -> (detalleCarrito.getNombreLocalidad().equals(editarCarritoDTO.nombreLocalidad()) &&
                        detalleCarrito.getIdEvento().equals(editarCarritoDTO.idEvento())))
                .findFirst();

        if (itemFiltrado.isPresent()){
            DetalleCarrito detalleCarrito = itemFiltrado.get();
            detalleCarrito.setCantidad(editarCarritoDTO.cantidadActulizada());
        }

        // Dónde vuelve a aumentar la cantidad de entradas disponibles?

        carritoRepo.save(carrito);

        return "Carrito editado exitosamente.";
    }

    @Override
    public String crearCarrito(String idUsuario) throws Exception {

        Usuario usuario = usuarioService.obtenerUsuario(idUsuario);

        Carrito carrito = Carrito.builder()
                .fecha(LocalDateTime.now())
                .itemsCarrito(new ArrayList<>())
                .idUsuario(new ObjectId(usuario.getId()))
                .build();

        carritoRepo.save(carrito);

        return "Carrito creado exitosamente";
    }

}
