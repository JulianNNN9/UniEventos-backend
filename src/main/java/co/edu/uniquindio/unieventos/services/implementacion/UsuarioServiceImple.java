package co.edu.uniquindio.unieventos.services.implementacion;

import co.edu.uniquindio.unieventos.config.JWTUtils;
import co.edu.uniquindio.unieventos.dto.EmailDTO;
import co.edu.uniquindio.unieventos.dto.TokenDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.*;
import co.edu.uniquindio.unieventos.exceptions.*;
import co.edu.uniquindio.unieventos.model.CodigoActivacion;
import co.edu.uniquindio.unieventos.model.EstadoUsuario;
import co.edu.uniquindio.unieventos.model.Rol;
import co.edu.uniquindio.unieventos.model.Usuario;
import co.edu.uniquindio.unieventos.repositories.UsuarioRepo;
import co.edu.uniquindio.unieventos.services.interfaces.EmailService;
import co.edu.uniquindio.unieventos.services.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class UsuarioServiceImple implements UsuarioService {

    private final EmailService emailService;
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final Duration LOCK_DURATION = Duration.ofMinutes(5);
    private final UsuarioRepo usuarioRepo;
    private final JWTUtils jwtUtils;

    @Override
    public String crearUsuario(CrearUsuarioDTO crearCuentaDTO) throws Exception {

        if (existeCedula(crearCuentaDTO.cedula())){
            throw new RecursoEncontradoException("La cedula ya está en uso");
        }

        if (existeEmail(crearCuentaDTO.email())){
            throw new RecursoEncontradoException("Este email ya está en uso");
        }

        Usuario nuevoUsuario = Usuario.builder()
                .cedula(crearCuentaDTO.cedula())
                .nombreCompleto(crearCuentaDTO.nombreCompleto())
                .direccion(crearCuentaDTO.direccion())
                .telefono(crearCuentaDTO.telefono())
                .email(crearCuentaDTO.email())
                .contrasenia(encriptarPassword(crearCuentaDTO.contrasenia()))
                .rol(Rol.CLIENTE)
                .estadoUsuario(EstadoUsuario.INACTIVA)
                .fechaRegistro(LocalDateTime.now())
                .codigoRegistro(
                        CodigoActivacion.builder()
                            .codigo("")
                            .fechaCreacion(LocalDateTime.now())
                        .build()
                )
        .build();

        Usuario usuarioGuardado = usuarioRepo.save(nuevoUsuario);
        EnviarCodigoActivacionAlCorreoDTO enviarCodigoActivacionAlCorreoDTO = new EnviarCodigoActivacionAlCorreoDTO(crearCuentaDTO.email());

        enviarCodigoActivacionCuenta(enviarCodigoActivacionAlCorreoDTO);
        return "" + usuarioGuardado.getId();

    }

    @Override
    public void editarUsuario(EditarUsuarioDTO editarCuentaDTO) throws RecursoNoEncontradoException {

        Usuario usuario = obtenerUsuario(editarCuentaDTO.idUsuario());
        
        usuario.setNombreCompleto( editarCuentaDTO.nombreCompleto() );
        usuario.setDireccion( editarCuentaDTO.direccion() );
        usuario.setTelefono( editarCuentaDTO.telefono() );

        usuarioRepo.save(usuario);
    }

    @Override
    public void eliminarUsuario(String id) throws RecursoNoEncontradoException {

        Usuario usuario = obtenerUsuario(id);

        usuario.setEstadoUsuario(EstadoUsuario.ELIMINADA);
        
        usuarioRepo.save(usuario);
    }

    @Override
    public InformacionUsuarioDTO obtenerInformacionUsuario(String id) throws RecursoNoEncontradoException {

        Usuario usuario = obtenerUsuario(id);

        if(usuario.getEstadoUsuario() == EstadoUsuario.ELIMINADA){
            throw new RecursoNoEncontradoException("Usuario no encontrado");
        }
        return new InformacionUsuarioDTO(
                usuario.getCedula(),
                usuario.getNombreCompleto(),
                usuario.getDireccion(),
                usuario.getTelefono(),
                usuario.getEmail()
        );
    }

    @Override
    public void enviarCodigoRecuperacionCuenta(EnviarCodigoRecuperacionAlCorreoDTO enviarCodigoRecuperacionAlCorreoDTO) throws Exception {

        Optional<Usuario> optionalUsuario = usuarioRepo.findByEmail(enviarCodigoRecuperacionAlCorreoDTO.correo());

        if (optionalUsuario.isEmpty()) {
            throw new RecursoNoEncontradoException("Email no encontrado");
        }

        Usuario usuario = optionalUsuario.get();

        CodigoActivacion codigoActivacion = CodigoActivacion.builder()
                .codigo(generarCodigoActivacion())
                .fechaCreacion(LocalDateTime.now())
                .build();

        usuario.setCodigoRecuperacionContrasenia(codigoActivacion);
        usuarioRepo.save(usuario);

        EmailDTO emailDTO = new EmailDTO(
                "Recuperacion de Cuenta",
                "Su Codigo de Recuperacion es: " + codigoActivacion.getCodigo(),
                enviarCodigoRecuperacionAlCorreoDTO.correo());

        emailService.enviarCorreo(emailDTO);
    }
    @Override
    public void enviarCodigoActivacionCuenta(EnviarCodigoActivacionAlCorreoDTO enviarCodigoActivacionAlCorreoDTO) throws Exception {

        Optional<Usuario> usuario = usuarioRepo.findByEmail(enviarCodigoActivacionAlCorreoDTO.correo());
        if(usuario.isEmpty()){
            throw new RecursoNoEncontradoException("Correo no encontrado");
        }
        Usuario usuarioActivacion = usuario.get();

        CodigoActivacion codigoActivacion= CodigoActivacion
                .builder()
                .codigo(generarCodigoActivacion())
                .fechaCreacion(LocalDateTime.now())
                .build();

        usuarioActivacion.setCodigoRegistro(codigoActivacion);
        usuarioRepo.save(usuarioActivacion);

        EmailDTO emailDTO = new EmailDTO(
                "Activacion de Cuenta",
                "Su Codigo de Activacion es: " + codigoActivacion.getCodigo(),
                enviarCodigoActivacionAlCorreoDTO.correo());

        emailService.enviarCorreo(emailDTO);


    }

    @Override
    public void recuperarContrasenia(RecuperarContraseniaDTO recuperarContraseniaDTO) throws RecursoNoEncontradoException, ContraseniaNoCoincidenException, CodigoExpiradoException, CodigoInvalidoException {

        Usuario usuario = obtenerUsuario(recuperarContraseniaDTO.idUsuario());
        if (!Objects.equals(recuperarContraseniaDTO.contraseniaNueva(), recuperarContraseniaDTO.confirmarContraseniaNueva())){
            throw new ContraseniaNoCoincidenException("Las contraseñas no coindicen");
        }

        if (usuario.getCodigoRecuperacionContrasenia().getFechaCreacion().plusMinutes(15).isBefore(LocalDateTime.now())){
            throw new CodigoExpiradoException("El código expiró");
        }

        if (!usuario.getCodigoRecuperacionContrasenia().getCodigo().equals(recuperarContraseniaDTO.codigoVerificacion())){
            throw new CodigoInvalidoException("El código es incorrecto");
        }

        usuario.setContrasenia(recuperarContraseniaDTO.contraseniaNueva());

        usuarioRepo.save(usuario);
    }

    @Override
    public void cambiarContrasenia(CambiarContraseniaDTO cambiarContraseniaDTO) throws RecursoNoEncontradoException, ContraseniaNoCoincidenException, ContraseniaIncorrectaException {

        Usuario usuario = obtenerUsuario(cambiarContraseniaDTO.idUsuario());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (!cambiarContraseniaDTO.contraseniaNueva().equals(cambiarContraseniaDTO.confirmarContraseniaNueva())){
            throw new ContraseniaNoCoincidenException("Las contraseñas no coindicen");
        }

        if( !passwordEncoder.matches(cambiarContraseniaDTO.contraseniaAntigua(), usuario.getContrasenia()) ) {
            throw new ContraseniaIncorrectaException("La contraseña es incorrecta");
        }

        usuario.setContrasenia(encriptarPassword(cambiarContraseniaDTO.contraseniaNueva()));

        usuarioRepo.save(usuario);
    }

    @Override
    public TokenDTO iniciarSesion(IniciarSesionDTO iniciarSesionDTO) throws Exception {

        Optional<Usuario> optionalUsuario = usuarioRepo.findByEmail(iniciarSesionDTO.email());

        if (optionalUsuario.isEmpty()){
            throw new RecursoNoEncontradoException("Usuario no Encontrado");
        }

        Usuario usuario = optionalUsuario.get();

        if (usuario.getEstadoUsuario() == EstadoUsuario.INACTIVA || usuario.getEstadoUsuario() == EstadoUsuario.ELIMINADA){
            throw new CuentaInactivaEliminadaException("Esta cuenta aún no ha sido activada o ha sido eliminada.");
        }

        //Manejo del bloqueo de cuenta
        if (estaBloqueada(usuario.getEmail())){
            throw new CuentaBloqueadaException("La cuenta se encuentra bloqueada por demasiados intentos, espere 5 minutos.");
        }

        if (usuario.getTiempoBloqueo() != null && LocalDateTime.now().isAfter(usuario.getTiempoBloqueo())){
            desbloquearUsuario(usuario.getEmail());
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


        if( !passwordEncoder.matches(iniciarSesionDTO.contrasenia(), usuario.getContrasenia()) ) {
            incrementarIntentosFallidos(usuario.getEmail());
            throw new Exception("La contraseña es incorrecta");
        }

        desbloquearUsuario(usuario.getEmail());

        Map<String, Object> map = construirClaims(usuario);

        return new TokenDTO( jwtUtils.generarToken(usuario.getEmail(), map) );
    }

    @Override
    public void activarCuenta(String codigoActivacion) throws Exception {
        Optional<Usuario> usuario = usuarioRepo.findByCodigoRegistroCodigo(codigoActivacion);
        if(usuario.isEmpty()){
            throw new RecursoNoEncontradoException("Codigo de activacion invalido");
        }
        Usuario usuarioActivacion = usuario.get();
        usuarioActivacion.setEstadoUsuario(EstadoUsuario.ACTIVA);
        usuarioRepo.save(usuarioActivacion);
    }

    @Override
    public Usuario obtenerUsuario(String id) throws RecursoNoEncontradoException {

        Optional<Usuario> optionalUsuario = usuarioRepo.findById(id);

        if(optionalUsuario.isEmpty()){
            throw new RecursoNoEncontradoException("Usuario no encontrado");
        }

        return optionalUsuario.get();
    }

    @Override
    public void incrementarIntentosFallidos(String correo) throws RecursoEncontradoException {
        Usuario usuario = obtenerUsuarioPorEmail(correo);
        usuario.setFallosInicioSesion(usuario.getFallosInicioSesion() + 1);

        if (usuario.getFallosInicioSesion() >= MAX_FAILED_ATTEMPTS) {
            usuario.setTiempoBloqueo(LocalDateTime.now().plus(LOCK_DURATION));
        }

        usuarioRepo.save(usuario);
    }

    public boolean estaBloqueada(String correo) throws RecursoEncontradoException {
        Usuario usuario = obtenerUsuarioPorEmail(correo);
        return usuario.getTiempoBloqueo() != null && LocalDateTime.now().isBefore(usuario.getTiempoBloqueo());
    }

    public void desbloquearUsuario(String username) throws RecursoEncontradoException {
        Usuario usuario = obtenerUsuarioPorEmail(username);
        usuario.setFallosInicioSesion(0);
        usuario.setTiempoBloqueo(null);
        usuarioRepo.save(usuario);
    }

    private Usuario obtenerUsuarioPorEmail(String correo) throws RecursoEncontradoException {
        Optional<Usuario> optionalUsuario = usuarioRepo.findByEmail(correo);

        if (optionalUsuario.isEmpty()){
            throw new RecursoEncontradoException("Usuario no encontrado.");
        }

        return optionalUsuario.get();
    }

    private boolean existeEmail(String email) {
        return usuarioRepo.findByEmail(email).isPresent();
    }

    private boolean existeCedula(String cedula) { return usuarioRepo.findByCedula(cedula).isPresent(); }

    private String generarCodigoActivacion(){

        String cadena = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        StringBuilder codigo = new StringBuilder();

        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            codigo.append(cadena.charAt(random.nextInt(cadena.length())));
        }

        return codigo.toString();
    }

    private String encriptarPassword(String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode( password );
    }

    private Map<String, Object> construirClaims(Usuario cuenta) {
        return Map.of(
                "rol", cuenta.getRol(),
                "nombre", cuenta.getNombreCompleto(),
                "id", cuenta.getId()
        );
    }



}
