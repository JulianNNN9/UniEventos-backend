package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.cupon.Crear_EditarCuponDTO;
import org.bson.types.ObjectId;

public interface CuponService {

    String crearCupon(Crear_EditarCuponDTO crearCuponDTO) throws Exception;

    String editarCupon(Crear_EditarCuponDTO crearCuponDTO)throws Exception;

    String eliminarCupon(String idCupon)throws Exception;

}
