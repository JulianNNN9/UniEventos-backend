package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.carrito.AgregarItemDTO;
import co.edu.uniquindio.unieventos.dto.carrito.EliminarDelCarritoDTO;

public interface CarritoService {

    String agregarAlCarrito(AgregarItemDTO agregarItemDTO) throws Exception;

    String eliminarDelCarrito(EliminarDelCarritoDTO eliminarDelCarritoDTO) throws Exception;

    String crearCarrito(String idUsuario) throws Exception;

}
