package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.dto.MensajeDTO;
import co.edu.uniquindio.unieventos.dto.TokenDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.CrearUsuarioDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.IniciarSesionDTO;
import co.edu.uniquindio.unieventos.services.interfaces.AutenticacionService;
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
    private final AutenticacionService autenticacionService;
    @PostMapping("/login-cliente")
    public ResponseEntity<MensajeDTO<TokenDTO>> iniciarSesionCliente(@Valid @RequestBody IniciarSesionDTO iniciarSesionDTO) throws Exception {
        TokenDTO tokenDTO = autenticacionService.iniciarSesionUsuario(iniciarSesionDTO);
        return ResponseEntity.ok().body(new MensajeDTO<>(false, tokenDTO));
    }
    @PostMapping("/registrar-usuarios")
    public ResponseEntity<MensajeDTO<String>> crearUsuario(@Valid @RequestBody CrearUsuarioDTO crearUsuarioDTO)throws Exception{
        usuarioService.crearUsuario(crearUsuarioDTO);
        return ResponseEntity.ok().body( new MensajeDTO<>(false, "Usuario registrado correctamente")
        );
    }
}
