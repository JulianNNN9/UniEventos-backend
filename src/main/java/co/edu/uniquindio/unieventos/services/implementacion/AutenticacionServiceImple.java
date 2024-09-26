package co.edu.uniquindio.unieventos.services.implementacion;

import co.edu.uniquindio.unieventos.config.JWTUtils;
import co.edu.uniquindio.unieventos.dto.TokenDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.CrearUsuarioDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.IniciarSesionDTO;
import co.edu.uniquindio.unieventos.exceptions.CuentaInactivaEliminadaException;
import co.edu.uniquindio.unieventos.exceptions.RecursoEncontradoException;
import co.edu.uniquindio.unieventos.exceptions.RecursoNoEncontradoException;
import co.edu.uniquindio.unieventos.model.CodigoValidacion;
import co.edu.uniquindio.unieventos.model.EstadoUsuario;
import co.edu.uniquindio.unieventos.model.Rol;
import co.edu.uniquindio.unieventos.model.Usuario;
import co.edu.uniquindio.unieventos.repositories.UsuarioRepo;
import co.edu.uniquindio.unieventos.services.interfaces.AutenticacionService;
import co.edu.uniquindio.unieventos.services.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AutenticacionServiceImple implements AutenticacionService {

    private final UsuarioRepo usuarioRepo;
    private final JWTUtils jwtUtils;

    @Override
    public TokenDTO iniciarSesionUsuario(IniciarSesionDTO iniciarSesionDTO) throws Exception {

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
    public TokenDTO iniciarSesionAdmin(IniciarSesionDTO iniciarSesionDTO) throws Exception {
        return null;
    }

    private Map<String, Object> construirClaims(Usuario cuenta) {
        return Map.of(
                "rol", cuenta.getRol(),
                "nombre", cuenta.getNombreCompleto(),
                "id", cuenta.getId()
        );
    }
}
