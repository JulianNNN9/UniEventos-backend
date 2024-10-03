package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.TokenDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.*;
import co.edu.uniquindio.unieventos.exceptions.*;
import co.edu.uniquindio.unieventos.model.Usuario;

public interface UsuarioService {

    String crearUsuario(CrearUsuarioDTO crearCuentaDTO) throws Exception;

    void editarUsuario(EditarUsuarioDTO editarCuentaDTO) throws Exception;

    void eliminarUsuario(String id) throws RecursoNoEncontradoException;

    InformacionUsuarioDTO obtenerInformacionUsuario(String id) throws RecursoNoEncontradoException;

    void enviarCodigoRecuperacionCuenta(EnviarCodigoRecuperacionAlCorreoDTO enviarCodigoRecuperacionAlCorreoDTO) throws Exception;

    void enviarCodigoActivacionCuenta(EnviarCodigoActivacionAlCorreoDTO enviarCodigoActivacionAlCorreoDTO) throws Exception;

    void recuperarContrasenia(RecuperarContraseniaDTO recuperarContraseniaDTO) throws Exception;

    void cambiarContrasenia(CambiarContraseniaDTO cambiarContraseniaDTO) throws Exception;

    Usuario obtenerUsuario(String id) throws Exception;

    Usuario obtenerUsuarioCorreo(String correo) throws RecursoNoEncontradoException;

    void incrementarIntentosFallidos(String correo) throws Exception;

    TokenDTO iniciarSesion(IniciarSesionDTO iniciarSesionDTO) throws Exception;

    void activarCuenta(String codigoActivacion) throws Exception;
}

