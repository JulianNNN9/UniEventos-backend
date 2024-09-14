package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.cuenta.*;
import co.edu.uniquindio.unieventos.model.Usuario;

public interface UsuarioService {

    void crearUsuario(CrearUsuarioDTO crearCuentaDTO) throws Exception;

    void editarUsuario(EditarUsuarioDTO editarCuentaDTO) throws Exception;

    void eliminarUsuario(String id) throws Exception;

    InformacionUsuarioDTO obtenerInformacionUsuario(String id) throws Exception;

    void enviarCodigoRecuperacionCuenta(EnviarCodigoAlCorreoDTO enviarCodigoRecuperacionCuentaDTO) throws Exception;

    void recuperarContrasenia(RecuperarContraseniaDTO recuperarContraseniaDTO) throws Exception;

    void cambiarContrasenia(CambiarContraseniaDTO cambiarContraseniaDTO) throws Exception;

    String iniciarSesion(IniciarSesionDTO iniciarSesionDTO) throws Exception;

    Usuario obtenerUsuario(String id) throws Exception;
}
