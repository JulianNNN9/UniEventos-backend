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

    Usuario obtenerUsuarioPorEmail(String correo) throws RecursoNoEncontradoException;

    void incrementarIntentosFallidos(String correo) throws RecursoNoEncontradoException;

    TokenDTO iniciarSesion(IniciarSesionDTO iniciarSesionDTO) throws RecursoNoEncontradoException,
            CuentaInactivaEliminadaException, CuentaBloqueadaException, ContraseniaIncorrectaException;

    void activarCuenta(String codigoActivacion) throws Exception;
}

