package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.dto.MensajeDTO;
import co.edu.uniquindio.unieventos.dto.TokenDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.*;
import co.edu.uniquindio.unieventos.services.interfaces.UsuarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.el.parser.Token;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@SecurityRequirement(name = "bearerAuth")

@RequiredArgsConstructor
public class UsuarioControlador {

    private final UsuarioService usuarioService;

    @PutMapping("/editar-perfil")
    public ResponseEntity<MensajeDTO<String>> editarUsuario(@Valid @RequestBody EditarUsuarioDTO editarUsuarioDTO)throws Exception{
        usuarioService.editarUsuario(editarUsuarioDTO);
        return ResponseEntity.ok().body( new MensajeDTO<>(false, "Cliente actualizado correctamente") );
    }

    @DeleteMapping("/eliminar/{codigo}")
    public ResponseEntity<MensajeDTO<String>> eliminarUsuario(@PathVariable String codigo)throws Exception{
        usuarioService.eliminarUsuario(codigo);
        return ResponseEntity.ok().body( new MensajeDTO<>(false, "Cliente eliminado correctamente")
        );
    }

    @GetMapping("/obtener/{codigo}")
    public ResponseEntity<MensajeDTO<InformacionUsuarioDTO>> obtenerInformacionUsuario(@PathVariable String codigo) throws Exception{
        return ResponseEntity.ok().body( new MensajeDTO<>(false,
                usuarioService.obtenerInformacionUsuario(codigo) ) );
    }

    @PostMapping("/enviar-codigo-recuperacion")
    public ResponseEntity<MensajeDTO<String>> enviarCodigoRecuperacionCuenta(@RequestBody EnviarCodigoRecuperacionAlCorreoDTO enviarCodigoRecuperacionAlCorreoDTO) throws Exception{
        usuarioService.enviarCodigoRecuperacionCuenta(enviarCodigoRecuperacionAlCorreoDTO);
        return ResponseEntity.ok().body( new MensajeDTO<>(false, "Código de recuperación enviado correctamente") );
    }
    @PostMapping("/enviar-codigo-activacion")
    public ResponseEntity<MensajeDTO<String>> enviarCodigoRecuperacionCuenta(@RequestBody EnviarCodigoActivacionAlCorreoDTO enviarCodigoActivacionAlCorreoDTO) throws Exception{
        usuarioService.enviarCodigoActivacionCuenta(enviarCodigoActivacionAlCorreoDTO);
        return ResponseEntity.ok().body( new MensajeDTO<>(false, "Código de activacion enviado correctamente") );
    }

    @PostMapping("/recuperar-contrasenia")
    public ResponseEntity<MensajeDTO<String>> recuperarContrasenia(@RequestBody RecuperarContraseniaDTO recuperarContraseniaDTO) throws Exception{
        usuarioService.recuperarContrasenia(recuperarContraseniaDTO);
        return ResponseEntity.ok().body( new MensajeDTO<>(false, "Contraseña recuperada correctamente") );
    }

    @PutMapping("/cambiar-contrasenia")
    public ResponseEntity<MensajeDTO<String>> cambiarContrasenia(@RequestBody CambiarContraseniaDTO cambiarContraseniaDTO) throws Exception{
        usuarioService.cambiarContrasenia(cambiarContraseniaDTO);
        return ResponseEntity.ok().body( new MensajeDTO<>(false, "Contraseña cambiada correctamente") );
    }



}