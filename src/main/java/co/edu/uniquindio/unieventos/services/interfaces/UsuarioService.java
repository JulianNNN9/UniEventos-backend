package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.cuenta.*;
import co.edu.uniquindio.unieventos.model.Usuario;

public interface UsuarioService {

    String crearUsuario(CrearUsuarioDTO crearCuentaDTO) throws Exception;

    void editarUsuario(EditarUsuarioDTO editarCuentaDTO) throws Exception;

    void eliminarUsuario(String id) throws Exception;

    InformacionUsuarioDTO obtenerInformacionUsuario(String id) throws Exception;

    String enviarCodigoRecuperacionCuenta(EnviarCodigoAlCorreoDTO enviarCodigoRecuperacionCuentaDTO) throws Exception;

    String recuperarContrasenia(RecuperarContraseniaDTO recuperarContraseniaDTO) throws Exception;

    String cambiarContrasenia(CambiarContraseniaDTO cambiarContraseniaDTO) throws Exception;

    String iniciarSesion(IniciarSesionDTO iniciarSesionDTO) throws Exception;

    Usuario obtenerUsuario(String id) throws Exception;
}
