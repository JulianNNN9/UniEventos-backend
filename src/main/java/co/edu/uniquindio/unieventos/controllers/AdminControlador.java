package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.dto.MensajeDTO;
import co.edu.uniquindio.unieventos.dto.cupon.CrearCuponDTO;
import co.edu.uniquindio.unieventos.dto.cupon.CuponDTO;
import co.edu.uniquindio.unieventos.dto.cupon.EditarCuponDTO;
import co.edu.uniquindio.unieventos.dto.evento.CrearEventoDTO;
import co.edu.uniquindio.unieventos.dto.evento.EditarEventoDTO;
import co.edu.uniquindio.unieventos.dto.evento.EliminarEventosDTO;
import co.edu.uniquindio.unieventos.services.interfaces.CuponService;
import co.edu.uniquindio.unieventos.services.interfaces.EventoService;
import co.edu.uniquindio.unieventos.services.interfaces.ImagenesService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@SecurityRequirement(name = "bearerAuth")
public class AdminControlador {

    private final ImagenesService imagenesService;
    private final EventoService eventoService;
    private final CuponService cuponService;

    @PostMapping ("/eventos/crear-evento")
    public ResponseEntity<MensajeDTO<String>> crearEvento(@Valid @RequestBody CrearEventoDTO crearEventoDTO) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, eventoService.crearEvento(crearEventoDTO)));
    }

    @PutMapping ("/eventos/editar-evento")
    public ResponseEntity<MensajeDTO<String>> editarEvento(@Valid @RequestBody EditarEventoDTO editarEventoDTO) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, eventoService.editarEvento(editarEventoDTO)));
    }

    @GetMapping ("/eventos/eliminar-evento/{idEvento}")
    public ResponseEntity<MensajeDTO<String>> eliminarEvento(@PathVariable String idEvento) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, eventoService.eliminarEvento(idEvento)));
    }
    @PostMapping("/eventos/eliminar-eventos")
    public ResponseEntity<MensajeDTO<String>> eliminarEventos(@RequestBody EliminarEventosDTO eliminarEventosDTO) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, eventoService.eliminarEventos(eliminarEventosDTO)));
    }

    @PostMapping("/imagenes/subir-imagen")
    public ResponseEntity<MensajeDTO<String>> subir(@RequestParam("imagen") MultipartFile imagen) throws Exception{
        String respuesta = imagenesService.subirImagen(imagen);
        return ResponseEntity.ok().body(new MensajeDTO<>(false, respuesta));
    }

    @PostMapping ("/cupon/crear-cupon")
    public ResponseEntity<MensajeDTO<String>> crearCupon(@Valid @RequestBody CrearCuponDTO crearCuponDTO) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, cuponService.crearCupon(crearCuponDTO)));
    }

    @PutMapping ("/cupon/editar-cupon")
    public ResponseEntity<MensajeDTO<String>> editarCupon(@Valid @RequestBody EditarCuponDTO editarCuponDTO) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, cuponService.editarCupon(editarCuponDTO)));
    }

    @GetMapping ("/cupon/eliminar-cupon/{idCupon}")
    public ResponseEntity<MensajeDTO<String>> eliminarCupon(@PathVariable String idCupon) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, cuponService.eliminarCupon(idCupon)));
    }

    @DeleteMapping("/imagenes/eliminar-imagen")
    public ResponseEntity<MensajeDTO<String>> eliminar(@RequestParam("idImagen") String idImagen)  throws Exception{
        imagenesService.eliminarImagen( idImagen );
        return ResponseEntity.ok().body(new MensajeDTO<>(false, "La imagen fue eliminada correctamente"));
    }

    @GetMapping ("/cupon/listar-cupones")
    public ResponseEntity<MensajeDTO<List<CuponDTO>>> listarCupones() throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, cuponService.listarCupones()));
    }
}
