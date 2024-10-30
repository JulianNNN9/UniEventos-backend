package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.dto.FiltrosEventosDTO;
import co.edu.uniquindio.unieventos.dto.MensajeDTO;
import co.edu.uniquindio.unieventos.dto.TokenDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.*;
import co.edu.uniquindio.unieventos.dto.evento.InformacionEventoDTO;
import co.edu.uniquindio.unieventos.dto.evento.ItemEventoDTO;
import co.edu.uniquindio.unieventos.dto.evento.NotificacionEventoDTO;
import co.edu.uniquindio.unieventos.services.interfaces.EventoService;
import co.edu.uniquindio.unieventos.services.interfaces.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publico")
@RequiredArgsConstructor
public class PublicoControlador {

    private final EventoService eventoService;
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

    @GetMapping("/eventos/obtener-informacion-evento/{idEvento}")
    public ResponseEntity<MensajeDTO<InformacionEventoDTO>> obtenerInformacionEvento(@PathVariable String idEvento) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, eventoService.obtenerInformacionEvento(idEvento)));
    }

    @PostMapping("/eventos/filtrar-evento")
    public ResponseEntity<MensajeDTO<List<ItemEventoDTO>>> filtrarEvento(@Valid @RequestBody FiltrosEventosDTO filtrosEventos) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, eventoService.filtrarEvento(filtrosEventos)));
    }

    @GetMapping ("/eventos/buscar-evento/{nombreEvento}")
    public ResponseEntity<MensajeDTO<List<ItemEventoDTO>>> buscarEvento(@PathVariable String nombreEvento) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, eventoService.buscarEventoPorNombre(nombreEvento)));
    }

    @GetMapping ("/eventos/listar-eventos")
    public ResponseEntity<MensajeDTO<List<ItemEventoDTO>>> listarEventos() throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, eventoService.listarEventos()));
    }

    @GetMapping ("/eventos/notificar-nuevo-evento")
    public ResponseEntity<MensajeDTO<List<NotificacionEventoDTO>>> notificarNuevoEvento() throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, eventoService.notificarNuevoEvento()));
    }

    @GetMapping("/enviar-codigo-recuperacion")
    public ResponseEntity<MensajeDTO<String>> enviarCodigoRecuperacionCuenta(@RequestParam String correo) throws Exception{
        usuarioService.enviarCodigoRecuperacionCuenta(correo);
        return ResponseEntity.ok().body( new MensajeDTO<>(false, "Si su Correo está registrado con nosotros, su código de recuperacion fue enviado correctamente") );
    }
    @GetMapping("/enviar-codigo-activacion")
    public ResponseEntity<MensajeDTO<String>> enviarCodigoActicacionCuenta(@RequestParam String correo) throws Exception{
        usuarioService.enviarCodigoActivacionCuenta(correo);
        return ResponseEntity.ok().body( new MensajeDTO<>(false, "Si su Correo está registrado con nosotros, su código de activacion fue enviado correctamente") );
    }

    @PostMapping("/activar-cuenta")
    public ResponseEntity<MensajeDTO<String>> activarCuenta(@RequestBody ActivarCuentaDTO activarCuentaDTO) throws Exception{
        usuarioService.activarCuenta(activarCuentaDTO);
        return ResponseEntity.ok().body( new MensajeDTO<>(false, "Cuenta activada correctamente") );
    }

    @PostMapping("/recuperar-contrasenia")
    public ResponseEntity<MensajeDTO<String>> recuperarContrasenia(@RequestBody RecuperarContraseniaDTO recuperarContraseniaDTO) throws Exception{
        usuarioService.recuperarContrasenia(recuperarContraseniaDTO);
        return ResponseEntity.ok().body( new MensajeDTO<>(false, "Contraseña recuperada correctamente") );
    }
}
