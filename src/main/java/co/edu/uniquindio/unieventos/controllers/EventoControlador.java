package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.dto.FiltrosEventosDTO;
import co.edu.uniquindio.unieventos.dto.MensajeDTO;
import co.edu.uniquindio.unieventos.dto.evento.*;
import co.edu.uniquindio.unieventos.model.Evento;
import co.edu.uniquindio.unieventos.services.interfaces.EventoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventos")
@RequiredArgsConstructor
public class EventoControlador {

    private final EventoService eventoService;

    @PostMapping ("/crear-evento")
    public ResponseEntity<MensajeDTO<String>> crearEvento(@Valid @RequestBody CrearEventoDTO crearEventoDTO) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, eventoService.crearEvento(crearEventoDTO)));
    }

    @PutMapping ("/editar-evento")
    public ResponseEntity<MensajeDTO<String>> editarEvento(@Valid @RequestBody EditarEventoDTO editarEventoDTO) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, eventoService.editarEvento(editarEventoDTO)));
    }

    @GetMapping ("/eliminar-evento/{idEvento}")
    public ResponseEntity<MensajeDTO<String>> eliminarEvento(@PathVariable String idEvento) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, eventoService.eliminarEvento(idEvento)));
    }

    @GetMapping ("/obtener-informacion-evento/{idEvento}")
    public ResponseEntity<MensajeDTO<InformacionEventoDTO>> obtenerInformacionEvento(@PathVariable String idEvento) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, eventoService.obtenerInformacionEvento(idEvento)));
    }

    @PostMapping ("/filtrar-evento")
    public ResponseEntity<MensajeDTO<List<ItemEventoDTO>>> filtrarEvento(@Valid @RequestBody FiltrosEventosDTO filtrosEventos) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, eventoService.filtrarEvento(filtrosEventos)));
    }

    @GetMapping ("/buscar-evento/{valorCampoDeBusqueda}")
    public ResponseEntity<MensajeDTO<List<ItemEventoDTO>>> buscarEvento(@PathVariable String valorCampoDeBusqueda) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, eventoService.buscarEvento(valorCampoDeBusqueda)));
    }

    @GetMapping ("/listar-eventos")
    public ResponseEntity<MensajeDTO<List<ItemEventoDTO>>> listarEventos() throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, eventoService.listarEventos()));
    }

    @GetMapping ("/obtener-evento/{idEvento}")
    public ResponseEntity<MensajeDTO<Evento>> obtenerEvento(@PathVariable String idEvento) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, eventoService.obtenerEvento(idEvento)));
    }

    @PostMapping ("/save-evento")
    public ResponseEntity<MensajeDTO<String>> saveEvento(@Valid @RequestBody Evento evento) throws Exception {
        eventoService.saveEvento(evento);
        return ResponseEntity.ok().body(new MensajeDTO<>(false, "Evento guardado correctamente"));
    }

    // Cada que se ingrese a la página principal se cargará la notificación de los eventos nuevos
    @GetMapping ("/notificar-nuevo-evento")
    public ResponseEntity<MensajeDTO<List<NotificacionEventoDTO>>> notificarNuevoEvento() throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, eventoService.notificarNuevoEvento()));
    }
}
