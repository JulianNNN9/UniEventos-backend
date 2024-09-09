package co.edu.uniquindio.unieventos.services.implementacion;


import co.edu.uniquindio.unieventos.dto.evento.CrearEventoDTO;
import co.edu.uniquindio.unieventos.dto.evento.EditarEventoDTO;
import co.edu.uniquindio.unieventos.dto.evento.InformacionEventoDTO;
import co.edu.uniquindio.unieventos.dto.evento.ItemEventoDTO;
import co.edu.uniquindio.unieventos.exceptions.RecursoEncontradoException;
import co.edu.uniquindio.unieventos.exceptions.RecursoNoEncontradoException;
import co.edu.uniquindio.unieventos.model.EstadoEvento;
import co.edu.uniquindio.unieventos.model.Evento;
import co.edu.uniquindio.unieventos.repositories.EventoRepo;
import co.edu.uniquindio.unieventos.services.interfaces.EventoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class EventoServiceImple implements EventoService {

    private final EventoRepo eventoRepo;

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
                .estadoEvento(EstadoEvento.ACTIVO)
                .build();

        eventoRepo.save(evento);

        return "Evento creado exitosamente.";
    }

    @Override
    public String editarEvento(EditarEventoDTO editarEventoDTO) throws Exception{

        Evento evento = obtenerEvento(editarEventoDTO.idEvento());

        evento.setNombreEvento(editarEventoDTO.nombreEvento());
        evento.setDireccionEvento(editarEventoDTO.direccionEvento());
        evento.setCiudadEvento(editarEventoDTO.ciudadEvento());
        evento.setDescripcionEvento(editarEventoDTO.descripcionEvento());
        evento.setFechaEvento(editarEventoDTO.fechaEvento());
        evento.setLocalidades(editarEventoDTO.localidades());
        evento.setImagenPortada(editarEventoDTO.imagenPortada());
        evento.setImagenLocalidades(editarEventoDTO.imagenLocalidades());
        evento.setEstadoEvento(editarEventoDTO.estadoEvento());

        eventoRepo.save(evento);

        return evento.getId();
    }

    @Override
    public String eliminarEvento(String idEvento) throws Exception {

        Evento evento = obtenerEvento(idEvento);

        evento.setEstadoEvento(EstadoEvento.INACTIVO);

        eventoRepo.save(evento);

        return "Evento desactivado exitosamente.";
    }

    @Override
    public InformacionEventoDTO obtenerInformacionEvento(String idEvento) throws Exception {

        Evento evento = obtenerEvento(idEvento);

        return new InformacionEventoDTO(
                evento.getNombreEvento(),
                evento.getDireccionEvento(),
                evento.getCiudadEvento(),
                evento.getDescripcionEvento(),
                evento.getFechaEvento(),
                evento.getLocalidades(),
                evento.getEstadoEvento()
        );
    }

    @Override
    public List<ItemEventoDTO> listarEventos() {

        List<Evento> eventos = eventoRepo.findAll();

        return eventos.stream()
                .map(evento -> new ItemEventoDTO(
                        evento.getId(),
                        evento.getNombreEvento(),
                        evento.getCiudadEvento(),
                        evento.getFechaEvento()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemEventoDTO> filtrarEvento() {
        // Implementación pendiente de acuerdo con los filtros requeridos.
        return null;
    }

    @Override
    public Evento obtenerEvento(String idEvento) throws Exception {

        Optional<Evento> eventoExistente = eventoRepo.findById(idEvento);

        if (eventoExistente.isEmpty()) {
            throw new RecursoNoEncontradoException("No encontrado con el ID: " + idEvento);
        }

        return eventoExistente.get();
    }

    @Override
    public void saveEvento(Evento evento) {
        eventoRepo.save(evento);
    }
}
