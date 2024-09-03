package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.carrito.AgregarItemDTO;
import co.edu.uniquindio.unieventos.model.DetalleCarrito;

public interface CarritoService {

    String agregarAlCarrito(AgregarItemDTO agregarItemDTO) throws Exception;//PREGUNTAR AL PROFESOR

    String eliminarDelCarrito(String id) throws Exception; //PREGUNTAR AL PROFESOR

    String crearCarrito() throws Exception;

}
