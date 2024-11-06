package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.dto.MensajeDTO;
import co.edu.uniquindio.unieventos.dto.TokenDTO;
import co.edu.uniquindio.unieventos.services.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AutenticacionControlador {

    private final UsuarioService usuarioService;

    @GetMapping("/refresh")
    public ResponseEntity<MensajeDTO<TokenDTO>> refresh(@RequestParam("token") String token) {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, usuarioService.refreshToken(token)));
    }
}
