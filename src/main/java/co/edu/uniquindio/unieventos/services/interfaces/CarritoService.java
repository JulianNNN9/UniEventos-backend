package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.carrito.AgregarItemDTO;
import co.edu.uniquindio.unieventos.dto.carrito.EditarCarritoDTO;
import co.edu.uniquindio.unieventos.dto.carrito.EliminarDelCarritoDTO;
import co.edu.uniquindio.unieventos.exceptions.RecursoNoEncontradoException;
import co.edu.uniquindio.unieventos.model.Carrito;

public interface CarritoService {

    String agregarAlCarrito(AgregarItemDTO agregarItemDTO) throws RecursoNoEncontradoException, IllegalArgumentException;

    String eliminarDelCarrito(EliminarDelCarritoDTO eliminarDelCarritoDTO) throws Exception;

    String editarCarrito(EditarCarritoDTO editarCarritoDTO) throws RecursoNoEncontradoException, IllegalArgumentException;

    String crearCarrito(String idUsuario) throws Exception;

    Carrito obtenerCarrito(String idCarrito) throws RecursoNoEncontradoException;

    Carrito obtenerCarritoPorIdUsuario(String idUsuario) throws RecursoNoEncontradoException;
}
