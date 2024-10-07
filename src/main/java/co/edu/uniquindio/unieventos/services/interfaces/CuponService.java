package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.cupon.CrearEditarCuponDTO;
import co.edu.uniquindio.unieventos.exceptions.RecursoEncontradoException;
import co.edu.uniquindio.unieventos.exceptions.RecursoNoEncontradoException;
import co.edu.uniquindio.unieventos.model.Cupon;

public interface CuponService {

    String crearCupon(CrearEditarCuponDTO crearCuponDTO) throws RecursoEncontradoException;

    String editarCupon(CrearEditarCuponDTO crearCuponDTO)throws RecursoNoEncontradoException;

    String eliminarCupon(String idCupon)throws RecursoNoEncontradoException;

    Cupon obtenerCuponPorCodigo(String id) throws RecursoNoEncontradoException;
    Cupon obtenerCuponPorId(String id) throws RecursoNoEncontradoException;
}
