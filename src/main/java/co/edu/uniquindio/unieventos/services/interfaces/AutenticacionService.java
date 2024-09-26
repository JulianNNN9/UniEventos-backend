package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.TokenDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.CrearUsuarioDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.IniciarSesionDTO;

public interface AutenticacionService {

    TokenDTO iniciarSesionUsuario(IniciarSesionDTO iniciarSesionDTO) throws Exception;

    TokenDTO iniciarSesionAdmin(IniciarSesionDTO iniciarSesionDTO) throws Exception;

}
