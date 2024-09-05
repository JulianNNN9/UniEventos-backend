package co.edu.uniquindio.unieventos.services.implementacion;

import co.edu.uniquindio.unieventos.dto.cuenta.*;
import co.edu.uniquindio.unieventos.exceptions.usuario.ContraseniaNoCoincidenException;
import co.edu.uniquindio.unieventos.exceptions.usuario.EmailEncontradoException;
import co.edu.uniquindio.unieventos.exceptions.usuario.UsuarioEcontradoException;
import co.edu.uniquindio.unieventos.exceptions.usuario.UsuarioNoEncontradoException;
import co.edu.uniquindio.unieventos.model.CodigoValidacion;
import co.edu.uniquindio.unieventos.model.EstadoUsuario;
import co.edu.uniquindio.unieventos.model.Rol;
import co.edu.uniquindio.unieventos.model.Usuario;
import co.edu.uniquindio.unieventos.repositories.UsuarioRepo;
import co.edu.uniquindio.unieventos.services.interfaces.UsuarioService;
import co.edu.uniquindio.unieventos.services.utility.EmailUtility;
import jakarta.mail.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class UsuarioServiceImple implements UsuarioService {

    private final UsuarioRepo usuarioRepo;
    
    private final EmailUtility emailUtility;

    public UsuarioServiceImple(UsuarioRepo usuarioRepo, EmailUtility emailUtility) {
        this.usuarioRepo = usuarioRepo;
        this.emailUtility = emailUtility;
    }

    private Usuario getUsuario(String id) throws UsuarioNoEncontradoException {
        Optional<Usuario> optionalUsuario = usuarioRepo.findById(id);

        if(optionalUsuario.isEmpty()){
            throw new UsuarioNoEncontradoException();
        }

        return optionalUsuario.get();
    }

    @Override
    public String crearUsuario(CrearUsuarioDTO crearCuentaDTO) throws UsuarioEcontradoException, EmailEncontradoException {

        if (existeCedula(crearCuentaDTO.cedula())){
            throw new UsuarioEcontradoException();
        }

        if (existeEmail(crearCuentaDTO.email())){
            throw new EmailEncontradoException();
        }

        String codigoActivacionCuenta = generarCodigoValidacion();

        Usuario nuevoUsuario = Usuario.builder()
                .cedula(crearCuentaDTO.cedula())
                .nombreCompleto(crearCuentaDTO.nombreCompleto())
                .direccion(crearCuentaDTO.direccion())
                .telefono(crearCuentaDTO.telefono())
                .email(crearCuentaDTO.email())
                .contrasenia(crearCuentaDTO.contrasenia())
                .rol(Rol.CLIENTE)
                .estadoUsuario(EstadoUsuario.INACTIVA)
                .fechaRegistro(LocalDateTime.now())
                .codigoRegistro(

                        CodigoValidacion.builder()
                            .codigo(codigoActivacionCuenta)
                            .fechaCreacion(LocalDateTime.now())
                        .build()
                )
        .build();

        // Enviar el correo con el código de activación usando la plantilla
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("subject", "Bienvenido a UniEventos");
        templateModel.put("body", "Gracias por registrarte en UniEventos. Tu código de confirmación es:");
        templateModel.put("code", nuevoUsuario.getCodigoRegistro().getCodigo());

        try {
            emailUtility.sendTemplateEmail(nuevoUsuario.getEmail(), "Confirma tu correo", templateModel);
        } catch (MessagingException e) {
            e.printStackTrace();

        }

        Usuario usuarioGuardado = usuarioRepo.save(nuevoUsuario);

        return usuarioGuardado.getId();
    }

    private boolean existeEmail(String email) {
        return usuarioRepo.findByEmail(email).isPresent();
    }

    private boolean existeCedula(String cedula) {
        return usuarioRepo.findByCedula(cedula).isPresent();
    }

    private String generarCodigoValidacion(){

        String cadena = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder codigo = new StringBuilder();

        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            codigo.append(cadena.charAt(random.nextInt(cadena.length())));
        }

        return codigo.toString();
    }

    @Override
    public String editarUsuario(EditarUsuarioDTO editarCuentaDTO) throws UsuarioNoEncontradoException {

        Usuario usuario = getUsuario(editarCuentaDTO.idUsuario());
        
        usuario.setNombreCompleto( editarCuentaDTO.nombreCompleto() );
        usuario.setDireccion( editarCuentaDTO.direccion() );
        usuario.setTelefono( editarCuentaDTO.telefono() );

        usuarioRepo.save(usuario);

        return "Su cuenta se ha actualizado correctamente";
    }

    @Override
    public String eliminarUsuario(String id) throws UsuarioNoEncontradoException {

        Usuario usuario = getUsuario(id);

        usuario.setEstadoUsuario(EstadoUsuario.ELIMINADA);
        
        usuarioRepo.save(usuario);
        
        return "Usuario se ha eliminado correctamente";
    }

    @Override
    public InformacionUsuarioDTO obtenerInformacionUsuario(String id) throws UsuarioNoEncontradoException {

        Usuario usuario = getUsuario(id);
        
        return new InformacionUsuarioDTO(
                usuario.getCedula(),
                usuario.getNombreCompleto(),
                usuario.getDireccion(),
                usuario.getTelefono(),
                usuario.getEmail()
        );
    }

    @Override
    public String enviarCodigoRecuperacionCuenta(EnviarCodigoAlCorreoDTO enviarCodigoRecuperacionCuentaDTO) throws Exception {

        Optional<Usuario> optionalUsuario = usuarioRepo.findByEmail(enviarCodigoRecuperacionCuentaDTO.correo());

        if (optionalUsuario.isEmpty()) {
            throw new Exception();
        }

        Usuario usuario = optionalUsuario.get();

        String codigoRecuperacionCuenta = generarCodigoValidacion();

        //TODO enviar codigo el usuario por correo

        usuario.setCodigoRecuperacionContrasenia(CodigoValidacion.builder()
                .codigo(codigoRecuperacionCuenta)
                .fechaCreacion(LocalDateTime.now())
                .build());

        usuarioRepo.save(usuario);

        return "Codigo enviado, expira en 15 minutos";
    }

    @Override
    public String recuperarContrasenia(RecuperarContraseniaDTO recuperarContraseniaDTO) throws Exception {

        Usuario usuario = getUsuario(recuperarContraseniaDTO.idUsuario());

        if (!Objects.equals(recuperarContraseniaDTO.contraseniaNueva(), recuperarContraseniaDTO.confirmarContraseniaNueva())){
            throw new ContraseniaNoCoincidenException();
        }

        if (usuario.getCodigoRecuperacionContrasenia().getFechaCreacion().plusMinutes(15).isAfter(LocalDateTime.now())){
            throw new Exception();
        }

        if (usuario.getCodigoRecuperacionContrasenia().getCodigo().equals(recuperarContraseniaDTO.codigoVerificacion())){
            usuario.setContrasenia(recuperarContraseniaDTO.contraseniaNueva());
        } else {
            throw new Exception();
        }

        usuarioRepo.save(usuario);

        return "Contrasenia recuperada exitosamente";
    }

    @Override
    public String cambiarContrasenia(CambiarContraseniaDTO cambiarContraseniaDTO) throws Exception {

        Usuario usuario = getUsuario(cambiarContraseniaDTO.idUsuario());

        if (cambiarContraseniaDTO.contraseniaAntigua().equals(usuario.getContrasenia())){
            if (cambiarContraseniaDTO.contraseniaAntigua().equals(cambiarContraseniaDTO.confirmarContraseniaNueva())){
                usuario.setContrasenia(usuario.getContrasenia());
            } else {
                throw new ContraseniaNoCoincidenException();
            }
        } else {
            throw new Exception();
        }

        return "Contrasenia cambiada exitosamente";
    }

    @Override
    public String iniciarSesion(IniciarSesionDTO iniciarSesionDTO) throws Exception {

        Optional<Usuario> optionalUsuario = usuarioRepo.validarInicioSesion(iniciarSesionDTO.email(), iniciarSesionDTO.contrasenia());

        if (optionalUsuario.isEmpty()){
            throw new UsuarioNoEncontradoException();
        }

        return "TOKEN_JWT";
    }
}