package co.edu.uniquindio.unieventos.services.implementacion;


import co.edu.uniquindio.unieventos.dto.evento.*;
import co.edu.uniquindio.unieventos.exceptions.RecursoNoEncontradoException;
import co.edu.uniquindio.unieventos.model.EstadoEvento;
import co.edu.uniquindio.unieventos.model.Evento;
import co.edu.uniquindio.unieventos.dto.FiltrosEventosDTO;
import co.edu.uniquindio.unieventos.repositories.EventoRepo;
import co.edu.uniquindio.unieventos.services.interfaces.EventoService;
import co.edu.uniquindio.unieventos.utils.TextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
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
                .nombreEvento(TextUtils.normalizarTexto(crearEventoDTO.nombreEvento()))
                .direccionEvento(TextUtils.normalizarTexto(crearEventoDTO.direccionEvento()))
                .ciudadEvento(TextUtils.normalizarTexto(crearEventoDTO.ciudadEvento()))
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
        return "Evento creado exitosamente";
    }

    @Override
    public String editarEvento(EditarEventoDTO editarEventoDTO) throws Exception{

        Evento evento = obtenerEvento(editarEventoDTO.idEvento());

        evento.setNombreEvento(TextUtils.normalizarTexto(editarEventoDTO.nombreEvento()));
        evento.setDireccionEvento(TextUtils.normalizarTexto(editarEventoDTO.direccionEvento()));
        evento.setCiudadEvento(TextUtils.normalizarTexto(editarEventoDTO.ciudadEvento()));
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
        return "Evento eliminado exitosamente";
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


    public Evento obtenerEvento(String idEvento) throws RecursoNoEncontradoException {

        Optional<Evento> eventoExistente = eventoRepo.findByIdAndEstadoNot(idEvento, EstadoEvento.ELIMINADO);

        if (eventoExistente.isEmpty()) {
            throw new RecursoNoEncontradoException("Evento no encontrado");
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
        return eventoRepo.findNuevosEventosAyerHoyAndEstadoNot(LocalDate.now().minusDays(1), LocalDate.now(), EstadoEvento.ELIMINADO);
    }

    @Override
    public List<ItemEventoDTO> filtrarEvento(FiltrosEventosDTO filtrosEventos) {
        String nombreEvento = filtrosEventos.nombreEvento();
        String tipoEvento = filtrosEventos.tipoEvento();
        String ciudadEvento = filtrosEventos.ciudadEvento();

        // Si todos los filtros son nulos o vacíos, devuelve todos los eventos (excepto los eliminados)
        if ((nombreEvento == null || nombreEvento.isEmpty()) &&
                (tipoEvento == null || tipoEvento.isEmpty()) &&
                (ciudadEvento == null || ciudadEvento.isEmpty())) {

            // Retorna todos los eventos que no están eliminados
            return eventoRepo.findByEstadoNot(EstadoEvento.ELIMINADO);
        }
        // Si todos los filtros son CORRECTOS
        if ((nombreEvento != null && !nombreEvento.isEmpty()) &&
                (tipoEvento != null && !tipoEvento.isEmpty()) &&
                (ciudadEvento != null && !ciudadEvento.isEmpty())) {

            // Retorna todos los eventos que no están eliminados
            return eventoRepo.findByNombreTipoCiudadAndEstadoNot(nombreEvento, tipoEvento, ciudadEvento, EstadoEvento.ELIMINADO);
        }


        // Filtrar combinaciones de dos parámetros
        if (nombreEvento != null && !nombreEvento.isEmpty() &&
                tipoEvento != null && !tipoEvento.isEmpty()) {
            // Combinación de nombre y tipo
            return eventoRepo.findByNombreTipoEventoAndEstadoNot(nombreEvento, tipoEvento, EstadoEvento.ELIMINADO);
        }

        if (nombreEvento != null && !nombreEvento.isEmpty() &&
                ciudadEvento != null && !ciudadEvento.isEmpty()) {
            // Combinación de nombre y ciudad
            return eventoRepo.findByNombreCiudadEventoAndEstadoNot(nombreEvento, ciudadEvento, EstadoEvento.ELIMINADO);
        }

        if (tipoEvento != null && !tipoEvento.isEmpty() &&
                ciudadEvento != null && !ciudadEvento.isEmpty()) {
            // Combinación de tipo y ciudad
            return eventoRepo.findByTipoCiudadEventoAndEstadoNot(tipoEvento, ciudadEvento, EstadoEvento.ELIMINADO);
        }

        // Filtrar individualmente si no hay combinaciones que aporten resultados
        if (nombreEvento != null && !nombreEvento.isEmpty()) {
            return eventoRepo.findByNombreEventoAndEstadoNot(nombreEvento, EstadoEvento.ELIMINADO);
        }

        if (tipoEvento != null && !tipoEvento.isEmpty()) {
            return eventoRepo.findByTipoEventoAndEstadoNot(tipoEvento, EstadoEvento.ELIMINADO);
        }

        if (ciudadEvento != null && !ciudadEvento.isEmpty()) {
            return eventoRepo.findByCiudadEventoAndEstadoNot(ciudadEvento, EstadoEvento.ELIMINADO);
        }

        // Si no se encontraron resultados, retorna una lista vacía o un valor por defecto
        return Collections.emptyList();
    }

    @Override
    public List<ItemEventoDTO> buscarEventoPorNombre(String nombreEvento) {
        List<ItemEventoDTO> eventosEncontrados = new ArrayList<>();
        if (!nombreEvento.isEmpty()) {
            eventosEncontrados = eventoRepo.findByNombreEventoAndEstadoNot(nombreEvento, EstadoEvento.ELIMINADO);
        }
        return eventosEncontrados;
    }
}