package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.TokenDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.*;
import co.edu.uniquindio.unieventos.exceptions.*;
import co.edu.uniquindio.unieventos.model.Usuario;

public interface UsuarioService {

    String crearUsuario(CrearUsuarioDTO crearCuentaDTO) throws Exception;

    void editarUsuario(EditarUsuarioDTO editarCuentaDTO) throws RecursoNoEncontradoException;

    void eliminarUsuario(String id) throws RecursoNoEncontradoException;

    InformacionUsuarioDTO obtenerInformacionUsuario(String id) throws RecursoNoEncontradoException;

    void enviarCodigoRecuperacionCuenta(EnviarCodigoRecuperacionAlCorreoDTO enviarCodigoRecuperacionAlCorreoDTO) throws Exception;

    void enviarCodigoActivacionCuenta(EnviarCodigoActivacionAlCorreoDTO enviarCodigoActivacionAlCorreoDTO) throws Exception;

    void recuperarContrasenia(RecuperarContraseniaDTO recuperarContraseniaDTO) throws RecursoNoEncontradoException, ContraseniaNoCoincidenException, CodigoExpiradoException, CodigoInvalidoException;

    void cambiarContrasenia(CambiarContraseniaDTO cambiarContraseniaDTO) throws RecursoNoEncontradoException, ContraseniaNoCoincidenException, ContraseniaIncorrectaException;

    TokenDTO iniciarSesion(IniciarSesionDTO iniciarSesionDTO) throws Exception;

    Usuario obtenerUsuario(String id) throws RecursoNoEncontradoException;
}
