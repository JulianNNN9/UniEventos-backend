package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.cupon.CuponDTO;
import co.edu.uniquindio.unieventos.exceptions.RecursoEncontradoException;
import co.edu.uniquindio.unieventos.exceptions.RecursoNoEncontradoException;
import co.edu.uniquindio.unieventos.dto.cupon.CrearCuponDTO;
import co.edu.uniquindio.unieventos.dto.cupon.EditarCuponDTO;
import co.edu.uniquindio.unieventos.model.Cupon;

import java.util.List;

public interface CuponService {

    String crearCupon(CrearCuponDTO crearCuponDTO) throws RecursoEncontradoException;

    String editarCupon(EditarCuponDTO editarCuponDTO)throws RecursoNoEncontradoException;

    String eliminarCupon(String idCupon)throws RecursoNoEncontradoException;

    Cupon obtenerCuponPorCodigo(String id) throws RecursoNoEncontradoException;

    Cupon obtenerCuponPorId(String id) throws RecursoNoEncontradoException;

    List<CuponDTO> listarCupones();

}
