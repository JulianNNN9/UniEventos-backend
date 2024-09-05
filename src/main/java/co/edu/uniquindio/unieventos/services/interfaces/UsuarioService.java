package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.cuenta.*;

public interface UsuarioService {

    String crearUsuario(CrearUsuarioDTO crearCuentaDTO) throws Exception;

    String editarUsuario(EditarUsuarioDTO editarCuentaDTO) throws Exception;

    String eliminarUsuario(String id) throws Exception;

    InformacionUsuarioDTO obtenerInformacionUsuario(String id) throws Exception;

    String enviarCodigoRecuperacionCuenta(EnviarCodigoAlCorreoDTO enviarCodigoRecuperacionCuentaDTO) throws Exception;

    String recuperarContrasenia(RecuperarContraseniaDTO recuperarContraseniaDTO) throws Exception;

    String cambiarContrasenia(CambiarContraseniaDTO cambiarContraseniaDTO) throws Exception;

    String iniciarSesion(IniciarSesionDTO iniciarSesionDTO) throws Exception;

}
