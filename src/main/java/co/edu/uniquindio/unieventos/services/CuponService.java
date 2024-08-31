package co.edu.uniquindio.unieventos.services;

import co.edu.uniquindio.unieventos.dto.cupon.Crear_EditarCuponDTO;
import org.bson.types.ObjectId;

public interface CuponService {

    String crearCupon(Crear_EditarCuponDTO crearCuponDTO) throws Exception;

    String editarCupon(Crear_EditarCuponDTO crearCuponDTO)throws Exception;

    String eliminarCupon(ObjectId idCupon)throws Exception;

    String validarCupon(String codigoCupon) throws Exception;
}
