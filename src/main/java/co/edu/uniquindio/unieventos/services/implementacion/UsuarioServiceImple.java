package co.edu.uniquindio.unieventos.services.implementacion;

import co.edu.uniquindio.unieventos.config.JWTUtils;
import co.edu.uniquindio.unieventos.dto.EmailDTO;
import co.edu.uniquindio.unieventos.dto.TokenDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.*;
import co.edu.uniquindio.unieventos.dto.cupon.CrearCuponDTO;
import co.edu.uniquindio.unieventos.exceptions.*;
import co.edu.uniquindio.unieventos.model.*;
import co.edu.uniquindio.unieventos.repositories.CarritoRepo;
import co.edu.uniquindio.unieventos.repositories.UsuarioRepo;
import co.edu.uniquindio.unieventos.services.interfaces.CuponService;
import co.edu.uniquindio.unieventos.services.interfaces.EmailService;
import co.edu.uniquindio.unieventos.services.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
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
    private final CuponService cuponService;
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final Duration LOCK_DURATION = Duration.ofMinutes(5);
    private final UsuarioRepo usuarioRepo;
    private final CarritoRepo carritoRepo;
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
                .primeraCompraRealizada(false)
        .build();

        Usuario usuarioGuardado = usuarioRepo.save(nuevoUsuario);
        EnviarCodigoActivacionAlCorreoDTO enviarCodigoActivacionAlCorreoDTO = new EnviarCodigoActivacionAlCorreoDTO(crearCuentaDTO.email());

        enviarCodigoActivacionCuenta(enviarCodigoActivacionAlCorreoDTO);

        Carrito carrito = Carrito.builder()
                .fecha(LocalDateTime.now())
                .idUsuario(usuarioGuardado.getId())
                .build();

        carritoRepo.save(carrito);

        return usuarioGuardado.getId();

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

        Usuario usuario= obtenerUsuarioPorEmail(enviarCodigoRecuperacionAlCorreoDTO.correo());

        CodigoRecuperacion codigoRecuperacion = CodigoRecuperacion.builder()
                .codigo(generarCodigoActivacion())
                .fechaCreacion(LocalDateTime.now())
                .build();

        usuario.setCodigoRecuperacion(codigoRecuperacion);
        usuarioRepo.save(usuario);

        EmailDTO emailDTO = new EmailDTO(
                "Recuperacion de Cuenta",
                "Su Codigo de Recuperacion es: " + codigoRecuperacion.getCodigo(),
                enviarCodigoRecuperacionAlCorreoDTO.correo());

        emailService.enviarCorreo(emailDTO);
    }
    @Override
    public void enviarCodigoActivacionCuenta(EnviarCodigoActivacionAlCorreoDTO enviarCodigoActivacionAlCorreoDTO) throws Exception {

        Usuario usuarioActivacion = obtenerUsuarioPorEmail(enviarCodigoActivacionAlCorreoDTO.correo());

        CodigoActivacion codigoActivacion= CodigoActivacion
                .builder()
                .codigo(generarCodigoActivacion())
                .fechaCreacion(LocalDateTime.now())
                .build();

        usuarioActivacion.setCodigoActivacion(codigoActivacion);
        usuarioRepo.save(usuarioActivacion);

        EmailDTO emailDTO = new EmailDTO(
                "Activacion de Cuenta",
                "Su Codigo de Activacion es: " + codigoActivacion.getCodigo(),
                enviarCodigoActivacionAlCorreoDTO.correo());

        emailService.enviarCorreo(emailDTO);


    }

    @Override
    public void recuperarContrasenia(RecuperarContraseniaDTO recuperarContraseniaDTO) throws RecursoNoEncontradoException, ContraseniaNoCoincidenException, CodigoExpiradoException, CodigoInvalidoException, RecursoEncontradoException {

        Usuario usuario = obtenerUsuarioPorEmail(recuperarContraseniaDTO.correoUsuario());
        if (!Objects.equals(recuperarContraseniaDTO.contraseniaNueva(), recuperarContraseniaDTO.confirmarContraseniaNueva())){
            throw new ContraseniaNoCoincidenException("Las contraseñas no coindicen");
        }

        if (usuario.getCodigoRecuperacion().getFechaCreacion().plusMinutes(15).isBefore(LocalDateTime.now())){
            throw new CodigoExpiradoException("El código expiró");
        }

        if (!usuario.getCodigoRecuperacion().getCodigo().equals(recuperarContraseniaDTO.codigoVerificacion())){
            throw new CodigoInvalidoException("El código es incorrecto");
        }

        usuario.setContrasenia(encriptarPassword(recuperarContraseniaDTO.contraseniaNueva()));

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
    public TokenDTO iniciarSesion(IniciarSesionDTO iniciarSesionDTO) throws RecursoNoEncontradoException,
            CuentaInactivaEliminadaException, CuentaBloqueadaException, ContraseniaIncorrectaException {

        Usuario usuario = obtenerUsuarioPorEmail(iniciarSesionDTO.email());

        if (usuario.getEstadoUsuario() == EstadoUsuario.INACTIVA){
            throw new CuentaInactivaEliminadaException("Esta cuenta aún no ha sido activada");
        }

        //Manejo del bloqueo de cuenta
        if (estaBloqueada(usuario.getEmail())){
            throw new CuentaBloqueadaException("La cuenta se encuentra bloqueada por demasiados intentos, espere 5 minutos");
        }

        if (usuario.getTiempoBloqueo() != null && LocalDateTime.now().isAfter(usuario.getTiempoBloqueo())){
            desbloquearUsuario(usuario.getEmail());
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


        if( !passwordEncoder.matches(iniciarSesionDTO.contrasenia(), usuario.getContrasenia()) ) {
            incrementarIntentosFallidos(usuario.getEmail());
            throw new ContraseniaIncorrectaException("La contraseña es incorrecta");
        }

        desbloquearUsuario(usuario.getEmail());

        Map<String, Object> map = construirClaims(usuario);

        return new TokenDTO( jwtUtils.generarToken(usuario.getEmail(), map) );
    }

    @Override
    public void activarCuenta(String codigoActivacion) throws Exception {
        Optional<Usuario> usuario = usuarioRepo.findByCodigoRegistroCodigoAndEstadoUsuarioNot(codigoActivacion, EstadoUsuario.ELIMINADA);
        if(usuario.isEmpty()){
            throw new RecursoNoEncontradoException("Codigo de activacion invalido");
        }
        Usuario usuarioActivacion = usuario.get();
        usuarioActivacion.setEstadoUsuario(EstadoUsuario.ACTIVA);

        String codigoCupon = cuponService.generarCodigoCupon();
        CrearCuponDTO cuponDTO = new CrearCuponDTO(codigoCupon, "cuponPrimerIngreso", 19.0, EstadoCupon.ACTIVO, TipoCupon.UNICO, LocalDate.now().plusDays(30));
        cuponService.crearCupon(cuponDTO);
        usuarioActivacion.getCuponesUsuario().add(cuponService.obtenerCuponPorCodigo(cuponDTO.codigo()));
        usuarioRepo.save(usuarioActivacion);

        EmailDTO emailDTO = new EmailDTO(
                "Tu nuevo cupon",
                "Por tu primer ingreso, tienes un nuevo cupon. Tu Codigo es: " + codigoCupon, usuarioActivacion.getEmail());
        emailService.enviarCorreo(emailDTO);

    }

    @Override
    public Usuario obtenerUsuario(String id) throws RecursoNoEncontradoException {

        Optional<Usuario> optionalUsuario = usuarioRepo.findByIdAndEstadoUsuarioNot(id, EstadoUsuario.ELIMINADA);

        if(optionalUsuario.isEmpty()){
            throw new RecursoNoEncontradoException("Usuario no encontrado");
        }
        return optionalUsuario.get();
    }

    @Override
    public Usuario obtenerUsuarioPorEmail(String correo) throws RecursoNoEncontradoException {

        Optional<Usuario> optionalUsuario = usuarioRepo.findByEmailAndEstadoUsuarioNot(correo, EstadoUsuario.ELIMINADA);

        if(optionalUsuario.isEmpty()){
            throw new RecursoNoEncontradoException("Email no encontrado");
        }
        return optionalUsuario.get();
    }

    @Override
    public void incrementarIntentosFallidos(String correo) throws RecursoNoEncontradoException {
        Usuario usuario = obtenerUsuarioPorEmail(correo);
        usuario.setFallosInicioSesion(usuario.getFallosInicioSesion() + 1);

        if (usuario.getFallosInicioSesion() >= MAX_FAILED_ATTEMPTS) {
            usuario.setTiempoBloqueo(LocalDateTime.now().plus(LOCK_DURATION));
        }

        usuarioRepo.save(usuario);
    }

    private boolean estaBloqueada(String correo) throws RecursoNoEncontradoException {
        Usuario usuario = obtenerUsuarioPorEmail(correo);
        return usuario.getTiempoBloqueo() != null && LocalDateTime.now().isBefore(usuario.getTiempoBloqueo());
    }

    private void desbloquearUsuario(String username) throws RecursoNoEncontradoException {
        Usuario usuario = obtenerUsuarioPorEmail(username);
        usuario.setFallosInicioSesion(0);
        usuario.setTiempoBloqueo(null);
        usuarioRepo.save(usuario);
    }

    private boolean existeEmail(String email) {

        return usuarioRepo.findByEmailAndEstadoUsuarioNot(email, EstadoUsuario.ELIMINADA).isPresent();
    }

    private boolean existeCedula(String cedula) { return usuarioRepo.findByCedulaAndEstadoUsuarioNot(cedula, EstadoUsuario.ELIMINADA).isPresent(); }

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
