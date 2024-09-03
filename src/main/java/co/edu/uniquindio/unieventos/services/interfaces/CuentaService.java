package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.cuenta.*;

public interface CuentaService {

    String crearCuenta(CrearCuentaDTO crearCuentaDTO) throws Exception;

    String editarCuenta(EditarCuentaDTO editarCuentaDTO) throws Exception;

    String eliminarCuenta(String id) throws Exception;

    InformacionCuentaDTO obtenerInformacionCuenta(String id) throws Exception;

    String enviarCodigoRecuperacionCuenta(String correo) throws Exception;

    String recuperarContrasenia(RecuperarContraseniaDTO recuperarContraseniaDTO) throws Exception;

    String cambiarContrasenia(CambiarContraseniaDTO cambiarContraseniaDTO) throws Exception;

    String iniciarSesion(IniciarSesionDTO iniciarSesionDTO) throws Exception;

}
