package co.edu.uniquindio.unieventos.services;

import co.edu.uniquindio.unieventos.dto.carrito.AgregarAlCarritoDTO;

public interface CarritoService {

    String agregarAlCarrito(AgregarAlCarritoDTO agregarAlCarritoDTO) throws Exception;//PREGUNTAR AL PROFESOR

    String eliminarDelCarrito() throws Exception; //PREGUNTAR AL PROFESOR

}
