package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.cupon.CrearEditarCuponDTO;
import co.edu.uniquindio.unieventos.model.Cupon;

public interface CuponService {

    String crearCupon(CrearEditarCuponDTO crearCuponDTO) throws Exception;

    String editarCupon(CrearEditarCuponDTO crearCuponDTO)throws Exception;

    String eliminarCupon(String idCupon)throws Exception;
  
    Cupon obtenerCupon(String id) throws Exception;
}
