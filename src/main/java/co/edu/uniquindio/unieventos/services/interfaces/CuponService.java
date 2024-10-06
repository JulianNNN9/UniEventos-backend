package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.cupon.CrearCuponDTO;
import co.edu.uniquindio.unieventos.dto.cupon.EditarCuponDTO;
import co.edu.uniquindio.unieventos.model.Cupon;

public interface CuponService {

    String crearCupon(CrearCuponDTO crearCuponDTO) throws Exception;

    String editarCupon(EditarCuponDTO crearCuponDTO)throws Exception;

    String eliminarCupon(String idCupon)throws Exception;
  
    Cupon obtenerCupon(String id) throws Exception;
}
