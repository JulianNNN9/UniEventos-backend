package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.dto.MensajeDTO;
import co.edu.uniquindio.unieventos.dto.TokenDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.CrearUsuarioDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.IniciarSesionDTO;
import co.edu.uniquindio.unieventos.services.interfaces.UsuarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/auth")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class AutenticacionControlador {

    private final UsuarioService usuarioService;

    @PostMapping("/iniciar-sesion")
    public ResponseEntity<MensajeDTO<TokenDTO>> iniciarSesion(@Valid @RequestBody IniciarSesionDTO
                                                                      iniciarSesionDTO) throws Exception{
        TokenDTO token = usuarioService.iniciarSesion(iniciarSesionDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, token));
    }

    @PostMapping("/registrar-usuarios")
    public ResponseEntity<MensajeDTO<String>> crearUsuario(@Valid @RequestBody CrearUsuarioDTO crearUsuarioDTO)throws Exception{
        usuarioService.crearUsuario(crearUsuarioDTO);
        return ResponseEntity.ok().body( new MensajeDTO<>(false, "Usuario registrado correctamente")
        );
    }
}
