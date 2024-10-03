package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.dto.MensajeDTO;
import co.edu.uniquindio.unieventos.dto.TokenDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.CrearUsuarioDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.IniciarSesionDTO;
import co.edu.uniquindio.unieventos.services.interfaces.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AutenticacionControlador {

    private final UsuarioService usuarioService;

    @PostMapping("/iniciar-sesion")
    public ResponseEntity<MensajeDTO<TokenDTO>> iniciarSesion(@RequestBody IniciarSesionDTO iniciarSesionDTO) throws Exception{
        return ResponseEntity.ok().body( new MensajeDTO<>(false, usuarioService.iniciarSesion(iniciarSesionDTO)) );
    }

    @PostMapping("/crear-usuario")
    public ResponseEntity<MensajeDTO<String>> crearUsuario(@Valid @RequestBody CrearUsuarioDTO crearUsuarioDTO)throws Exception{
        usuarioService.crearUsuario(crearUsuarioDTO);
        return ResponseEntity.ok().body( new MensajeDTO<>(false, "Usuario registrado correctamente")
        );
    }


}
