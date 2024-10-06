package co.edu.uniquindio.unieventos.services.implementacion;


import co.edu.uniquindio.unieventos.dto.evento.*;
import co.edu.uniquindio.unieventos.exceptions.RecursoNoEncontradoException;
import co.edu.uniquindio.unieventos.model.EstadoEvento;
import co.edu.uniquindio.unieventos.model.Evento;
import co.edu.uniquindio.unieventos.dto.FiltrosEventosDTO;
import co.edu.uniquindio.unieventos.model.TipoEvento;
import co.edu.uniquindio.unieventos.repositories.EventoRepo;
import co.edu.uniquindio.unieventos.services.interfaces.EventoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@Service
@Transactional
@RequiredArgsConstructor
public class EventoServiceImple implements EventoService {

    private final EventoRepo eventoRepo;
    private final MongoTemplate mongoTemplate;

    @Override
    public String crearEvento(CrearEventoDTO crearEventoDTO) {

        Evento evento = Evento.builder()
                .nombreEvento(crearEventoDTO.nombreEvento())
                .direccionEvento(crearEventoDTO.direccionEvento())
                .ciudadEvento(crearEventoDTO.ciudadEvento())
                .descripcionEvento(crearEventoDTO.descripcionEvento())
                .tipoEvento(crearEventoDTO.tipoEvento())
                .fechaEvento(LocalDateTime.of(crearEventoDTO.fechaEvento(), LocalTime.MIDNIGHT))
                .fechaCreacion(LocalDate.now())
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
        evento.setFechaCreacion(LocalDate.now());
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
        evento.setEstadoEvento(EstadoEvento.ELIMINADO);
        eventoRepo.save(evento);
        return "Evento eliminado exitosamente.";
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

    /* Se usaría cada vez que se cree un nuevo evento
    * idealmente, en los eventos que se creen, se notificará a los usuarios
    *
    */
    @Override
    public List<NotificacionEventoDTO> notificarNuevoEvento() throws Exception {
        return eventoRepo.findNuevosEventosAyerHoy(LocalDate.now().minusDays(1), LocalDate.now());
    }

    @Override
    public List<ItemEventoDTO> filtrarEvento(FiltrosEventosDTO filtrosEventos) {
        return eventoRepo.findByNombreTipoCiudad(filtrosEventos.nombreEvento(), null, "");
    }

    public List<ItemEventoDTO> buscarEvento(String valorCampoDeBusqueda) {
        List<ItemEventoDTO> eventosEncontrados = new ArrayList<>();
        if (!valorCampoDeBusqueda.isEmpty()) {
            eventosEncontrados = eventoRepo.findByNombreEvento(valorCampoDeBusqueda);
        }
        return eventosEncontrados;
    }
}