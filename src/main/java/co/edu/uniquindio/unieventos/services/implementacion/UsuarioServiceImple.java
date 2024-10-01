package co.edu.uniquindio.unieventos.services.implementacion;

import co.edu.uniquindio.unieventos.config.JWTUtils;
import co.edu.uniquindio.unieventos.dto.TokenDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.*;
import co.edu.uniquindio.unieventos.exceptions.*;
import co.edu.uniquindio.unieventos.model.CodigoValidacion;
import co.edu.uniquindio.unieventos.model.EstadoUsuario;
import co.edu.uniquindio.unieventos.model.Rol;
import co.edu.uniquindio.unieventos.model.Usuario;
import co.edu.uniquindio.unieventos.repositories.UsuarioRepo;
import co.edu.uniquindio.unieventos.services.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class UsuarioServiceImple implements UsuarioService {

    private final UsuarioRepo usuarioRepo;
    private final JWTUtils jwtUtils;

    @Override
    public void crearUsuario(CrearUsuarioDTO crearCuentaDTO) throws Exception {

        if (existeCedula(crearCuentaDTO.cedula())){
            throw new RecursoEncontradoException("El cedula ya existe");
        }

        if (existeEmail(crearCuentaDTO.email())){
            throw new RecursoEncontradoException("Este email ya existe.");
        }

        String codigoActivacionCuenta = generarCodigoValidacion();

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

                        CodigoValidacion.builder()
                            .codigo(codigoActivacionCuenta)
                            .fechaCreacion(LocalDateTime.now())
                        .build()
                )
        .build();

        usuarioRepo.save(nuevoUsuario);

    }

    @Override
    public void editarUsuario(EditarUsuarioDTO editarCuentaDTO) throws Exception {

        Usuario usuario = obtenerUsuario(editarCuentaDTO.idUsuario());
        
        usuario.setNombreCompleto( editarCuentaDTO.nombreCompleto() );
        usuario.setDireccion( editarCuentaDTO.direccion() );
        usuario.setTelefono( editarCuentaDTO.telefono() );

        usuarioRepo.save(usuario);
    }

    @Override
    public void eliminarUsuario(String id) throws Exception {

        Usuario usuario = obtenerUsuario(id);

        usuario.setEstadoUsuario(EstadoUsuario.ELIMINADA);
        
        usuarioRepo.save(usuario);
    }

    @Override
    public InformacionUsuarioDTO obtenerInformacionUsuario(String id) throws Exception {

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
    public void enviarCodigoRecuperacionCuenta(EnviarCodigoAlCorreoDTO enviarCodigoRecuperacionCuentaDTO) throws Exception {

        Optional<Usuario> optionalUsuario = usuarioRepo.findByEmail(enviarCodigoRecuperacionCuentaDTO.correo());

        if (optionalUsuario.isEmpty()) {
            throw new Exception();
        }

        Usuario usuario = optionalUsuario.get();


        //TODO enviar codigo el usuario por correo

        usuario.setCodigoRecuperacionContrasenia(CodigoValidacion.builder()
                .codigo("a")
                .fechaCreacion(LocalDateTime.now())
                .build());

        usuarioRepo.save(usuario);
    }

    @Override
    public void recuperarContrasenia(RecuperarContraseniaDTO recuperarContraseniaDTO) throws Exception {

        Usuario usuario = obtenerUsuario(recuperarContraseniaDTO.idUsuario());

        if (!Objects.equals(recuperarContraseniaDTO.contraseniaNueva(), recuperarContraseniaDTO.confirmarContraseniaNueva())){
            throw new ContraseniaNoCoincidenException("Las contraseñas no coindicen.");
        }

        if (usuario.getCodigoRecuperacionContrasenia().getFechaCreacion().plusMinutes(15).isAfter(LocalDateTime.now())){
            throw new CodigoExpiradoException("El código expiró");
        }

        if (!usuario.getCodigoRecuperacionContrasenia().getCodigo().equals(recuperarContraseniaDTO.codigoVerificacion())){
            throw new CodigoInvalidoException("El código es incorrecto");
        }

        usuario.setContrasenia(recuperarContraseniaDTO.contraseniaNueva());

        usuarioRepo.save(usuario);
    }

    @Override
    public void cambiarContrasenia(CambiarContraseniaDTO cambiarContraseniaDTO) throws Exception {

        Usuario usuario = obtenerUsuario(cambiarContraseniaDTO.idUsuario());

        if (!cambiarContraseniaDTO.contraseniaAntigua().equals(usuario.getContrasenia())){
            throw new ContraseniaNoCoincidenException("Las contraseña es incorrecta.");
        }

        if (!cambiarContraseniaDTO.contraseniaNueva().equals(cambiarContraseniaDTO.confirmarContraseniaNueva())){
            throw new ContraseniaNoCoincidenException("Las contraseñas no coindicen.");
        }

        usuario.setContrasenia(usuario.getContrasenia());

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

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


        if( !passwordEncoder.matches(iniciarSesionDTO.contrasenia(), usuario.getContrasenia()) ) {
            throw new Exception("La contraseña es incorrecta");
        }

        Map<String, Object> map = construirClaims(usuario);

        return new TokenDTO( jwtUtils.generarToken(usuario.getEmail(), map) );
    }

    @Override
    public Usuario obtenerUsuario(String id) throws Exception {

        Optional<Usuario> optionalUsuario = usuarioRepo.findById(id);

        if(optionalUsuario.isEmpty()){
            throw new RecursoNoEncontradoException("Usuario no encontrado.");
        }

        return optionalUsuario.get();
    }

    private boolean existeEmail(String email) {
        return usuarioRepo.findByEmail(email).isPresent();
    }

    private boolean existeCedula(String cedula) { return usuarioRepo.findByCedula(cedula).isPresent(); }

    private String generarCodigoValidacion(){

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
