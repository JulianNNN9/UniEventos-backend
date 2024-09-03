package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.cuenta.*;
import co.edu.uniquindio.unieventos.exceptions.usuario.EmailEncontradoException;
import co.edu.uniquindio.unieventos.exceptions.usuario.UsuarioEcontradoException;
import co.edu.uniquindio.unieventos.exceptions.usuario.UsuarioNoEncontradoException;

public interface UsuarioService {

    String crearUsuario(CrearUsuarioDTO crearCuentaDTO) throws UsuarioEcontradoException, EmailEncontradoException;

    String editarUsuario(EditarUsuarioDTO editarCuentaDTO) throws UsuarioNoEncontradoException;

    String eliminarUsuario(String id) throws UsuarioNoEncontradoException;

    InformacionUsuarioDTO obtenerInformacionUsuario(String id) throws UsuarioNoEncontradoException;

    String enviarCodigoRecuperacionCuenta(String correo) throws Exception;

    String recuperarContrasenia(RecuperarContraseniaDTO recuperarContraseniaDTO) throws Exception;

    String cambiarContrasenia(CambiarContraseniaDTO cambiarContraseniaDTO) throws Exception;

    String iniciarSesion(IniciarSesionDTO iniciarSesionDTO) throws Exception;

}
