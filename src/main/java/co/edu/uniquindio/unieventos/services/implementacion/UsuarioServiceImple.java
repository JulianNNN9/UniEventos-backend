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
import jakarta.mail.MessagingException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;

@Service
@Transactional
public class UsuarioServiceImple implements UsuarioService {

    private final UsuarioRepo usuarioRepo;
    private final EmailServiceImple emailServiceImple;

    public UsuarioServiceImple(UsuarioRepo usuarioRepo, EmailServiceImple emailServiceImple) {
        this.usuarioRepo = usuarioRepo;
        this.emailServiceImple = emailServiceImple;
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
            emailServiceImple.sendTemplateEmail(nuevoUsuario.getEmail(), "Confirma tu correo", templateModel);
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

        Optional<Usuario> optionalUsuario = usuarioRepo.findById("editarCuentaDTO.codigo()");

        if(optionalUsuario.isEmpty()){
            throw new UsuarioNoEncontradoException();
        }

        Usuario usuario = optionalUsuario.get();
        usuario.setNombreCompleto( editarCuentaDTO.nombreCompleto() );
        usuario.setDireccion( editarCuentaDTO.direccion() );
        usuario.setTelefono( editarCuentaDTO.telefono() );

        usuarioRepo.save(usuario);

        return "";
    }

    @Override
    public String eliminarUsuario(String id) throws UsuarioNoEncontradoException {

        Optional<Usuario> optionalUsuario = usuarioRepo.findById(id);

        if(optionalUsuario.isEmpty()){
            throw new UsuarioNoEncontradoException();
        }

        Usuario usuario = optionalUsuario.get();
        usuario.setEstadoUsuario(EstadoUsuario.INACTIVA);
        usuarioRepo.save(usuario);
        return "";
    }

    @Override
    public InformacionUsuarioDTO obtenerInformacionUsuario(String id) throws UsuarioNoEncontradoException {

        Optional<Usuario> optionalUsuario = usuarioRepo.findById( id );

        if(optionalUsuario.isEmpty()){
            throw new UsuarioNoEncontradoException();
        }

        Usuario usuario = optionalUsuario.get();
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

        String destinatario = enviarCodigoRecuperacionCuentaDTO.correo();
        String asunto = "Recuperación de Contraseña - UniEventos";
        // Cuerpo del mensaje en HTML
        String cuerpoMensaje = String.format(
                "<html>" +
                        "<head>" +
                        "<style>" +
                        ".container {" +
                        "font-family: Arial, sans-serif;" +
                        "text-align: center;" +
                        "margin: 0 auto;" +
                        "padding: 20px;" +
                        "width: 80%%;" +  // Usa '%%' para representar '%' en una cadena format.
                        "max-width: 600px;" +
                        "border: 1px solid #ddd;" +
                        "border-radius: 8px;" +
                        "background-color: #f9f9f9;" +
                        "}" +
                        ".header {" +
                        "font-size: 24px;" +
                        "color: #333;" +
                        "margin-bottom: 20px;" +
                        "}" +
                        ".content {" +
                        "font-size: 16px;" +
                        "color: #555;" +
                        "margin-bottom: 30px;" +
                        "}" +
                        ".footer {" +
                        "font-size: 14px;" +
                        "color: #777;" +
                        "margin-top: 30px;" +
                        "}" +
                        ".image-container {" +
                        "text-align: center;" +
                        "margin-top: 20px;" +
                        "}" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<div class='container'>" +
                        "<div class='header'>" +
                        "Recuperación de Contraseña - UniEventos" +
                        "</div>" +
                        "<div class='content'>" +
                        "<p>Hemos recibido una solicitud para restablecer tu contraseña.</p>" +
                        "<p>Tu código de recuperación es: <strong>%s</strong></p>" +
                        "<p>Este código expira en 15 minutos, así que asegúrate de usarlo antes de que expire.</p>" +
                        "</div>" +
                        "<div class='footer'>" +
                        "<p>Si no solicitaste un cambio de contraseña, por favor ignora este correo.</p>" +
                        "<p>Gracias por confiar en UniEventos.</p>" +
                        "</div>" +
                        "<div class='image-container'>" +
                        "<img src='URL_DE_LA_IMAGEN' alt='Imagen de UniEventos' style='max-width: 100%%; height: auto;'>" +
                        "</div>" +
                        "</div>" +
                        "</body>" +
                        "</html>",
                enviarCodigoRecuperacionCuentaDTO.codigo()
        );

        // Configuración del servidor de correo
        Properties propiedades = new Properties();
        propiedades.put("mail.smtp.host", "smtp.gmail.com"); // Cambia esto por tu servidor SMTP
        propiedades.put("mail.smtp.port", "587"); // Cambia esto por el puerto adecuado
        propiedades.put("mail.smtp.auth", "true");
        propiedades.put("mail.smtp.starttls.enable", "true");

        // Autenticación del correo
        String usuario = "tuEmail@example.com";
        String contrasena = "tuContraseña";

        Session sesion = Session.getInstance(propiedades, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usuario, contrasena);
            }
        });

        try {
            Message mensaje = new MimeMessage(sesion);
            mensaje.setFrom(new InternetAddress(usuario));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensaje.setSubject(asunto);
            mensaje.setContent(cuerpoMensaje, "text/html; charset=utf-8");

            Transport.send(mensaje);
            return "Correo de recuperación enviado exitosamente";

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al enviar el correo de recuperación", e);
        }
    }

    @Override
    public String enviarCodigoActivacionCuenta(EnviarCodigoAlCorreoDTO enviarCodigoActivacionCuentaDTO) throws Exception {

        String destinatario = enviarCodigoActivacionCuentaDTO.correo();
        String asunto = "Activación de Cuenta";
        String cuerpoMensaje = String.format(
                "<html>" +
                        "<head>" +
                        "<style>" +
                        ".container {" +
                        "font-family: Arial, sans-serif;" +
                        "text-align: center;" +
                        "margin: 0 auto;" +
                        "padding: 20px;" +
                        "width: 80%%;" +  // Usa '%%' para representar '%' en una cadena format.
                        "max-width: 600px;" +
                        "border: 1px solid #ddd;" +
                        "border-radius: 8px;" +
                        "background-color: #f9f9f9;" +
                        "}" +
                        ".header {" +
                        "font-size: 24px;" +
                        "color: #333;" +
                        "margin-bottom: 20px;" +
                        "}" +
                        ".content {" +
                        "font-size: 16px;" +
                        "color: #555;" +
                        "margin-bottom: 30px;" +
                        "}" +
                        ".footer {" +
                        "font-size: 14px;" +
                        "color: #777;" +
                        "margin-top: 30px;" +
                        "}" +
                        ".image-container {" +
                        "text-align: center;" +
                        "margin-top: 20px;" +
                        "}" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<div class='container'>" +
                        "<div class='header'>" +
                        "Bienvenido a UniEventos" +
                        "</div>" +
                        "<div class='content'>" +
                        "<p>Gracias por registrarte en nuestra página. Estamos emocionados de que formes parte de nuestra comunidad.</p>" +
                        "<p>Tu código de activación es: <strong>%s</strong></p>" +
                        "<p>Este código expira en 15 minutos, así que asegúrate de activarlo antes de que expire.</p>" +
                        "</div>" +
                        "<div class='footer'>" +
                        "<p>¡Gracias por elegir UniEventos! Esperamos verte pronto.</p>" +
                        "</div>" +
                        "<div class='image-container'>" +
                        "<img src='URL_DE_LA_IMAGEN' alt='Imagen de UniEventos' style='max-width: 100%%; height: auto;'>" +
                        "</div>" +
                        "</div>" +
                        "</body>" +
                        "</html>",
                enviarCodigoActivacionCuentaDTO.codigo()
        );

        // Configuración del servidor de correo
        Properties propiedades = new Properties();
        propiedades.put("mail.smtp.host", "smtp.gmail.com"); // Cambia esto por tu servidor SMTP
        propiedades.put("mail.smtp.port", "587"); // Cambia esto por el puerto adecuado
        propiedades.put("mail.smtp.auth", "true");
        propiedades.put("mail.smtp.starttls.enable", "true");

        // Autenticación del correo
        String usuario = "tuEmail@example.com";
        String contrasena = "tuContraseña";

        Session sesion = Session.getInstance(propiedades, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usuario, contrasena);
            }
        });

        try {

            Message mensaje = new MimeMessage(sesion);
            mensaje.setFrom(new InternetAddress(usuario));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensaje.setSubject(asunto);
            mensaje.setContent(cuerpoMensaje, "text/html; charset=utf-8");

            Transport.send(mensaje);
            return "Correo enviado exitosamente";

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al enviar el correo", e);
        }

    }

    @Override
    public String recuperarContrasenia(RecuperarContraseniaDTO recuperarContraseniaDTO) throws Exception {

        Optional<Usuario> optionalUsuario = usuarioRepo.findById("editarCuentaDTO.codigo()");

        if(optionalUsuario.isEmpty()){
            throw new UsuarioNoEncontradoException();
        }

        Usuario usuario = optionalUsuario.get();

        if (!Objects.equals(recuperarContraseniaDTO.contraseniaNueva(), recuperarContraseniaDTO.confirmarContraseniaNueva())){
            throw new ContraseniaNoCoincidenException();
        }

        if (usuario.getCodigoRecuperacionContrasenia().getCodigo().equals(recuperarContraseniaDTO.codigoVerificacion())){
            usuario.setContrasenia(recuperarContraseniaDTO.contraseniaNueva());
        }

        return "Contrasenia recuperada exitosamente";
    }

    @Override
    public String cambiarContrasenia(CambiarContraseniaDTO cambiarContraseniaDTO) throws Exception {
        return "";
    }

    @Override
    public String iniciarSesion(IniciarSesionDTO iniciarSesionDTO) throws Exception {
        return "";
    }
}
