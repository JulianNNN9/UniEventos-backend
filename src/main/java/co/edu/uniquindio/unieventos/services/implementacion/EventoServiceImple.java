package co.edu.uniquindio.unieventos.services.implementacion;


import co.edu.uniquindio.unieventos.dto.evento.CrearEventoDTO;
import co.edu.uniquindio.unieventos.dto.evento.EditarEventoDTO;
import co.edu.uniquindio.unieventos.dto.evento.InformacionEventoDTO;
import co.edu.uniquindio.unieventos.dto.evento.ItemEventoDTO;
import co.edu.uniquindio.unieventos.model.EstadoEvento;
import co.edu.uniquindio.unieventos.model.Evento;
import co.edu.uniquindio.unieventos.repositories.EventoRepo;
import co.edu.uniquindio.unieventos.repositories.UsuarioRepo;
import co.edu.uniquindio.unieventos.services.interfaces.EventoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventoServiceImple implements EventoService {

    private final EventoRepo eventoRepo;
    public EventoServiceImple(EventoRepo eventoRepo) {
        this.eventoRepo = eventoRepo;
    }

    @Override
    public String crearEvento(CrearEventoDTO crearEventoDTO) {

        Evento evento = Evento.builder()
                .nombreEvento(crearEventoDTO.nombreEvento())
                .direccionEvento(crearEventoDTO.direccionEvento())
                .ciudadEvento(crearEventoDTO.ciudadEvento())
                .descripcionEvento(crearEventoDTO.descripcionEvento())
                .tipoEvento(crearEventoDTO.tipoEvento())
                .fechaEvento(LocalDateTime.of(crearEventoDTO.fechaEvento(), LocalTime.MIDNIGHT))
                .localidades(crearEventoDTO.localidades())
                .imagenPortada(crearEventoDTO.imagenPortada())
                .imagenLocalidades(crearEventoDTO.imagenLocalidades())
                .estadoEvento(EstadoEvento.ACTIVO) // Se establece un estado inicial al crear
                .build();

        eventoRepo.save(evento);

        return evento.getId();
    }

    @Override
    public String editarEvento(EditarEventoDTO editarEventoDTO) {

        Optional<Evento> eventoExistente = eventoRepo.findById(editarEventoDTO.nombreEvento());

        if (eventoExistente.isPresent()) {
            Evento evento = eventoExistente.get();
            evento.setDireccionEvento(editarEventoDTO.direccionEvento());
            evento.setCiudadEvento(editarEventoDTO.ciudadEvento());
            evento.setDescripcionEvento(editarEventoDTO.descripcionEvento());
            evento.setFechaEvento(LocalDateTime.of(editarEventoDTO.fechaEvento(), LocalTime.MIDNIGHT));
            evento.setLocalidades(editarEventoDTO.localidades());
            evento.setImagenPortada(editarEventoDTO.imagenPortada());
            evento.setImagenLocalidades(editarEventoDTO.imagenLocalidades());
            evento.setEstadoEvento(editarEventoDTO.estadoEvento());

            eventoRepo.save(evento);
            return evento.getId();

        } else {
            return "Evento no encontrado.";
        }
    }

    @Override
    public String eliminarEvento(String id) {

        if (eventoRepo.existsById(id)) {

            eventoRepo.deleteById(id);
            return "Evento eliminado con éxito.";
        } else {
            return "Evento no encontrado.";
        }
    }

    @Override
    public InformacionEventoDTO obtenerInformacionEvento(String idEvento) {
        Optional<Evento> eventoOpt = eventoRepo.findById(idEvento);

        if (eventoOpt.isPresent()) {
            Evento evento = eventoOpt.get();
            return new InformacionEventoDTO(
                    evento.getNombreEvento(),
                    evento.getDireccionEvento(),
                    evento.getCiudadEvento(),
                    evento.getDescripcionEvento(),
                    evento.getFechaEvento().toLocalDate(),  // Convertir LocalDateTime a LocalDate
                    evento.getLocalidades(),
                    evento.getEstadoEvento()
            );
        } else {
            throw new NoSuchElementException("Evento no encontrado con el ID: " + idEvento);
        }
    }

    @Override
    public List<ItemEventoDTO> listarEventos() {

        List<Evento> eventos = eventoRepo.findAll();

        return eventos.stream()
                .map(evento -> new ItemEventoDTO(
                        evento.getId(),
                        evento.getNombreEvento(),
                        evento.getCiudadEvento(),
                        evento.getFechaEvento().toLocalDate()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemEventoDTO> filtrarEvento() {
        // Implementación pendiente de acuerdo con los filtros requeridos.
        return null;
    }
}
