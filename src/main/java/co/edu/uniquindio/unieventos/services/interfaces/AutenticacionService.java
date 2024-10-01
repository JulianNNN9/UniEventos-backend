package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.TokenDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.CrearUsuarioDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.IniciarSesionDTO;

public interface AutenticacionService {

    TokenDTO iniciarSesion(IniciarSesionDTO iniciarSesionDTO) throws Exception;

}
