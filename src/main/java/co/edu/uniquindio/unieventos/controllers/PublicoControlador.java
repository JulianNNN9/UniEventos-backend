package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.dto.FiltrosEventosDTO;
import co.edu.uniquindio.unieventos.dto.MensajeDTO;
import co.edu.uniquindio.unieventos.dto.TokenDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.*;
import co.edu.uniquindio.unieventos.dto.cupon.CuponDTO;
import co.edu.uniquindio.unieventos.dto.evento.InformacionEventoDTO;
import co.edu.uniquindio.unieventos.dto.evento.ItemEventoDTO;
import co.edu.uniquindio.unieventos.dto.evento.NotificacionEventoDTO;
import co.edu.uniquindio.unieventos.services.interfaces.CuponService;
import co.edu.uniquindio.unieventos.services.interfaces.EventoService;
import co.edu.uniquindio.unieventos.services.interfaces.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publico")
@RequiredArgsConstructor
public class PublicoControlador {

    private final EventoService eventoService;
    private final UsuarioService usuarioService;
    private final CuponService cuponService;

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

    @PostMapping("/eventos/filtrar-evento-item")
    public ResponseEntity<MensajeDTO<List<ItemEventoDTO>>> filtrarEventoItem(@Valid @RequestBody FiltrosEventosDTO filtrosEventos) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, eventoService.filtrarEventoItem(filtrosEventos)));
    }
    @PostMapping("/eventos/filtrar-evento-info")
    public ResponseEntity<MensajeDTO<List<InformacionEventoDTO>>> filtrarEventoInfo(@Valid @RequestBody FiltrosEventosDTO filtrosEventos) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, eventoService.filtrarEventoInfo(filtrosEventos)));
    }

    @GetMapping ("/eventos/buscar-evento/{nombreEvento}")
    public ResponseEntity<MensajeDTO<List<ItemEventoDTO>>> buscarEvento(@PathVariable String nombreEvento) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, eventoService.buscarEventoPorNombre(nombreEvento)));
    }

    @GetMapping("/eventos/listar-eventos-paginados-item")
    public ResponseEntity<MensajeDTO<List<ItemEventoDTO>>> listarEventosPaginadosItem(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "12") int tamano) throws Exception {

        List<ItemEventoDTO> eventosPaginados = eventoService.listarEventosPaginadosItem(pagina, tamano);
        return ResponseEntity.ok(new MensajeDTO<>(false, eventosPaginados));
    }
    @GetMapping("/eventos/listar-eventos-paginados-info")
    public ResponseEntity<MensajeDTO<List<InformacionEventoDTO>>> listarEventosPaginadosInfo(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "12") int tamano) throws Exception {

        List<InformacionEventoDTO> eventosPaginados = eventoService.listarEventosPaginadosInfo(pagina, tamano);
        return ResponseEntity.ok(new MensajeDTO<>(false, eventosPaginados));
    }
    @GetMapping ("/eventos/listar-eventos")
    public ResponseEntity<MensajeDTO<List<InformacionEventoDTO>>> listarEventos() throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, eventoService.listarEventos()));
    }
    @GetMapping("/cupon/obtener-cupon/{idCupon}")
    public ResponseEntity<MensajeDTO<CuponDTO>> obtenerCuponDTO(@PathVariable String idCupon) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, cuponService.obtenerCuponDTO(idCupon)));
    }
    @GetMapping ("/cupon/listar-cupones")
    public ResponseEntity<MensajeDTO<List<CuponDTO>>> listarCupones() throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, cuponService.listarCupones()));
    }
    @GetMapping ("/eventos/listar-ciudades")
    public ResponseEntity<MensajeDTO<List<String>>> listarCiudades() throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, eventoService.obtenerTipoCiudades()));
    }
    @GetMapping ("/eventos/listar-tipo-eventos")
    public ResponseEntity<MensajeDTO<List<String>>> listarTipoEventos() throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, eventoService.obtenerTiposEventos()));
    }
    @GetMapping ("/eventos/listar-estado-eventos")
    public ResponseEntity<MensajeDTO<List<String>>> listarEstadoEventos() throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, eventoService.obtenerEstadoEventos()));
    }
    @GetMapping ("/cupon/listar-tipo-cupones")
    public ResponseEntity<MensajeDTO<List<String>>> listarTipoCupones() throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, cuponService.obtenerTiposCupones()));
    }
    @GetMapping ("/cupon/listar-estado-cupones")
    public ResponseEntity<MensajeDTO<List<String>>> listarEstadoCupones() throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, cuponService.obtenerEstadoCupones()));
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
