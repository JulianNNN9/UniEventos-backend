package co.edu.uniquindio.unieventos.services.implementacion;


import co.edu.uniquindio.unieventos.dto.evento.*;
import co.edu.uniquindio.unieventos.exceptions.RecursoNoEncontradoException;
import co.edu.uniquindio.unieventos.model.EstadoEvento;
import co.edu.uniquindio.unieventos.model.Evento;
import co.edu.uniquindio.unieventos.dto.FiltrosEventosDTO;
import co.edu.uniquindio.unieventos.model.TipoCiudad;
import co.edu.uniquindio.unieventos.model.TipoEvento;
import co.edu.uniquindio.unieventos.repositories.EventoRepo;
import co.edu.uniquindio.unieventos.services.interfaces.EventoService;
import co.edu.uniquindio.unieventos.utils.TextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
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
        Evento evento1 = eventoRepo.save(evento);

        return evento1.getId();
    }

    @Override
    public String editarEvento(EditarEventoDTO editarEventoDTO) throws Exception{

        Evento evento = obtenerEvento(editarEventoDTO.idEvento());

        evento.setNombreEvento(TextUtils.normalizarTexto(editarEventoDTO.nombreEvento()));
        evento.setDireccionEvento(TextUtils.normalizarTexto(editarEventoDTO.direccionEvento()));
        evento.setCiudadEvento(TextUtils.normalizarTexto(editarEventoDTO.ciudadEvento()));
        evento.setDescripcionEvento(editarEventoDTO.descripcionEvento());
        evento.setTipoEvento(editarEventoDTO.tipoEvento());
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
    public String eliminarEventos(EliminarEventosDTO eliminarEventosDTO) throws Exception {
        for (String idEvento : eliminarEventosDTO.listaIdEventos()) {
            Evento evento = obtenerEvento(idEvento);
            evento.setEstadoEvento(EstadoEvento.ELIMINADO);
            eventoRepo.save(evento);
        }
        return "Eventos eliminados exitosamente";
    }


    @Override
    public InformacionEventoDTO obtenerInformacionEvento(String idEvento) throws Exception {

        Evento evento = obtenerEvento(idEvento);

        return new InformacionEventoDTO(
                evento.getId(),
                evento.getNombreEvento(),
                evento.getDireccionEvento(),
                evento.getCiudadEvento(),
                evento.getDescripcionEvento(),
                ""+evento.getTipoEvento(),
                evento.getFechaEvento(),
                evento.getLocalidades(),
                evento.getImagenPortada(),
                evento.getImagenLocalidades(),
                "" + evento.getEstadoEvento()
        );
    }

    @Override
    public List<InformacionEventoDTO> listarEventos() {

        List<Evento> eventos = eventoRepo.findByFechaEventoAfterAndEstadoEvento(LocalDateTime.now(), EstadoEvento.ACTIVO);

        return eventos.stream()
                .map(evento -> new InformacionEventoDTO(
                        evento.getId(),
                        evento.getNombreEvento(),
                        evento.getDireccionEvento(),
                        evento.getCiudadEvento(),
                        evento.getDescripcionEvento(),
                        "" + evento.getTipoEvento(),
                        evento.getFechaEvento(),
                        evento.getLocalidades(),
                        evento.getImagenPortada(),
                        evento.getImagenLocalidades(),
                        "" + evento.getEstadoEvento()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemEventoDTO> listarEventosPaginadosItem(int pagina, int tamano) {
        Pageable pageable = PageRequest.of(pagina, tamano);
        List<Evento> eventos = eventoRepo.findByFechaEventoAfterAndEstadoEventoPaginado(LocalDateTime.now(), EstadoEvento.ACTIVO, pageable);

        return eventos.stream()
                .map(evento -> new ItemEventoDTO(
                        evento.getId(),
                        evento.getNombreEvento(),
                        evento.getDireccionEvento(),
                        evento.getCiudadEvento(),
                        evento.getFechaEvento(),
                        evento.getImagenPortada()
                ))
                .collect(Collectors.toList());
    }
    @Override
    public List<InformacionEventoDTO> listarEventosPaginadosInfo(int pagina, int tamano) {
        Pageable pageable = PageRequest.of(pagina, tamano);
        List<Evento> eventos = eventoRepo.findByFechaEventoAfterAndEstadoEventoPaginado(LocalDateTime.now(), EstadoEvento.ACTIVO, pageable);

        return eventos.stream()
                .map(evento -> new InformacionEventoDTO(
                        evento.getId(),
                        evento.getNombreEvento(),
                        evento.getDireccionEvento(),
                        evento.getCiudadEvento(),
                        evento.getDescripcionEvento(),
                        ""+evento.getTipoEvento(),
                        evento.getFechaEvento(),
                        evento.getLocalidades(),
                        evento.getImagenPortada(),
                        evento.getImagenLocalidades(),
                        "" + evento.getEstadoEvento()
                ))
                .collect(Collectors.toList());
    }

    public Evento obtenerEvento(String idEvento) throws RecursoNoEncontradoException {
        if (idEvento.length() != 24) {
            if (idEvento.length() < 24) {
                // Si es más corto, completar con ceros al final
                idEvento = String.format("%-24s", idEvento).replace(' ', '0');
            } else {
                // Si es más largo, recortar a 24 caracteres
                idEvento = idEvento.substring(0, 24);
            }
        }
        Optional<Evento> eventoExistente = eventoRepo.findByIdAndEstadoNot(idEvento, EstadoEvento.ELIMINADO);

        if (eventoExistente.isEmpty()) {
            throw new RecursoNoEncontradoException("Evento no encontrado o ELIMINADO");
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
        List<Evento> eventos = eventoRepo.findNuevosEventosAyerHoyAndEstadoNot(LocalDate.now().minusDays(1), LocalDate.now(), EstadoEvento.ELIMINADO);

        return eventos.stream()
                .map(evento -> new NotificacionEventoDTO(
                        evento.getId(),
                        evento.getNombreEvento(),
                        "" + evento.getFechaEvento(),
                        evento.getDescripcionEvento(),
                        evento.getCiudadEvento(),
                        evento.getImagenPortada()

                ))
                .collect(Collectors.toList());
    }

    private List<Evento> filtrarEvento(FiltrosEventosDTO filtrosEventos) {
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
    public List<ItemEventoDTO> filtrarEventoItem(FiltrosEventosDTO filtrosEventos){
        List<Evento> eventos = filtrarEvento(filtrosEventos);
        return eventos.stream().map((evento) -> new ItemEventoDTO(
                evento.getId(),
                evento.getNombreEvento(),
                evento.getDireccionEvento(),
                evento.getCiudadEvento(),
                evento.getFechaEvento(),
                evento.getImagenPortada()
        )).collect(Collectors.toList());
    }
    @Override
    public List<String> obtenerTipoCiudades() {
        return Arrays.stream(TipoCiudad.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> obtenerTiposEventos() {
        return Arrays.stream(TipoEvento.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
    @Override
    public List<String> obtenerEstadoEventos() {
        return Arrays.stream(EstadoEvento.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
    @Override
    public List<InformacionEventoDTO> filtrarEventoInfo(FiltrosEventosDTO filtrosEventos){
        List<Evento> eventos = filtrarEvento(filtrosEventos);
        return eventos.stream().map((evento) -> new InformacionEventoDTO(
                evento.getId(),
                evento.getNombreEvento(),
                evento.getDireccionEvento(),
                evento.getCiudadEvento(),
                evento.getDescripcionEvento(),
                ""+evento.getTipoEvento(),
                evento.getFechaEvento(),
                evento.getLocalidades(),
                evento.getImagenPortada(),
                evento.getImagenLocalidades(),
                "" + evento.getEstadoEvento()
        )).collect(Collectors.toList());
    }

    @Override
    public List<ItemEventoDTO> buscarEventoPorNombre(String nombreEvento) {
        List<Evento> eventosEncontrados = null;
        if (nombreEvento != null && !nombreEvento.isEmpty()) {
            eventosEncontrados = eventoRepo.findByNombreEventoAndEstadoNot(nombreEvento, EstadoEvento.ELIMINADO);
        }
        return eventosEncontrados.stream().map((evento) -> new ItemEventoDTO(
                evento.getId(),
                evento.getNombreEvento(),
                evento.getDireccionEvento(),
                evento.getCiudadEvento(),
                evento.getFechaEvento(),
                evento.getImagenPortada()
        )).collect(Collectors.toList());
    }
}