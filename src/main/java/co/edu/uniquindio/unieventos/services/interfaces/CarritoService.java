package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.carrito.AgregarItemDTO;
import co.edu.uniquindio.unieventos.dto.carrito.EditarCarritoDTO;
import co.edu.uniquindio.unieventos.dto.carrito.EliminarDelCarritoDTO;
import co.edu.uniquindio.unieventos.model.Carrito;

public interface CarritoService {

    String agregarAlCarrito(AgregarItemDTO agregarItemDTO) throws Exception;

    String eliminarDelCarrito(EliminarDelCarritoDTO eliminarDelCarritoDTO) throws Exception;

    String editarCarrito(EditarCarritoDTO editarCarritoDTO) throws Exception;

    String crearCarrito(String idUsuario) throws Exception;

    Carrito obtenerCarrito(String idCarrito) throws Exception;

}
