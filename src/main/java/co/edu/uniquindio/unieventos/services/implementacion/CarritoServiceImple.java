package co.edu.uniquindio.unieventos.services.implementacion;

import co.edu.uniquindio.unieventos.dto.carrito.AgregarItemDTO;
import co.edu.uniquindio.unieventos.dto.carrito.EliminarDelCarritoDTO;
import co.edu.uniquindio.unieventos.exceptions.RecursoNoEncontradoException;
import co.edu.uniquindio.unieventos.model.Carrito;
import co.edu.uniquindio.unieventos.model.DetalleCarrito;
import co.edu.uniquindio.unieventos.repositories.CarritoRepo;
import co.edu.uniquindio.unieventos.repositories.UsuarioRepo;
import co.edu.uniquindio.unieventos.services.interfaces.CarritoService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CarritoServiceImple implements CarritoService {

    private final CarritoRepo carritoRepo;
    private final UsuarioRepo usuarioRepo;

    public CarritoServiceImple(CarritoRepo carritoRepo, UsuarioRepo usuarioRepo) {
        this.carritoRepo = carritoRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @Override
    public String agregarAlCarrito(AgregarItemDTO agregarItemDTO) throws Exception {

        Optional<Carrito> carritoExistente = carritoRepo.findById(agregarItemDTO.idCarrito());

        if (carritoExistente.isPresent()) {

            Carrito carrito = carritoExistente.get();

            carrito.getItemsCarrito().add(agregarItemDTO.detalleCarrito());

            carritoRepo.save(carrito);

            return "Item agregado al carrito con éxito.";
        } else {
            throw new RecursoNoEncontradoException("Carrito no encontrado con el ID: " + agregarItemDTO.idCarrito());
        }
    }

    @Override
    public String eliminarDelCarrito(EliminarDelCarritoDTO eliminarDelCarritoDTO) throws Exception {

        Optional<Carrito> carritoExistente = carritoRepo.findById(eliminarDelCarritoDTO.idCarrito());

        if (carritoExistente.isPresent()) {

            Carrito carrito = carritoExistente.get();

            List<DetalleCarrito> itemsFiltrados = carrito.getItemsCarrito().stream()
                    .filter(item -> !(item.getNombreLocalidad().equals(eliminarDelCarritoDTO.nombreLocalidad()) &&
                            item.getIdEvento().equals(eliminarDelCarritoDTO.idEvento())))
                    .collect(Collectors.toList());

            carrito.setItemsCarrito(itemsFiltrados);

            carritoRepo.save(carrito);

            return "Item eliminado del carrito con éxito.";
        } else {
            throw new RecursoNoEncontradoException("Carrito no encontrado con el ID: " + eliminarDelCarritoDTO.idCarrito());
        }
    }

    @Override
    public String crearCarrito(String idUsuario) {

        //Validar que el id del usuario exista

        if (usuarioRepo.findById(idUsuario).isPresent()){
            Carrito carrito = Carrito.builder()
                    .fecha(LocalDateTime.now())
                    .itemsCarrito(new ArrayList<>())
                    .idUsuario(idUsuario)
                    .build();
            carritoRepo.save(carrito);

            return "Carrito creado exitosamente";
        }
        else{
            return "El usuario no existe";
        }
    }

}
