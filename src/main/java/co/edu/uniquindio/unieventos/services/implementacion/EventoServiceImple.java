package co.edu.uniquindio.unieventos.services.implementacion;


import co.edu.uniquindio.unieventos.dto.evento.CrearEventoDTO;
import co.edu.uniquindio.unieventos.dto.evento.EditarEventoDTO;
import co.edu.uniquindio.unieventos.dto.evento.InformacionEventoDTO;
import co.edu.uniquindio.unieventos.dto.evento.ItemEventoDTO;
import co.edu.uniquindio.unieventos.model.EstadoEvento;
import co.edu.uniquindio.unieventos.model.Evento;
import co.edu.uniquindio.unieventos.model.FiltrosEventos;
import co.edu.uniquindio.unieventos.repositories.EventoRepo;
import co.edu.uniquindio.unieventos.repositories.UsuarioRepo;
import co.edu.uniquindio.unieventos.services.interfaces.EventoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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

    /*Este metodo recibe como parámetro los filtros seleccionados por el usuario, en
    puede recibir de uno a tres tipos de filtros (Ciudad, Nombre y Tipo). Tenemos
    también una lista de enumeraciones, las cuales serán los valores seleccionados de
    cada tipo de filtro. Para que se lleve un orden, idealmente, se llevará así
    1. Nombre, 2. Ciudad, 3, Tipo. Para que los filtros no se intercambien.
     */
    // Importante: Se debe seguir al pie de la letra el orden de los filtros
    @Override
    public List<ItemEventoDTO> filtrarEvento(List<FiltrosEventos> tipoFiltrosSeleccionados, List<Enum<?>> valoresFiltrosSeleccionados) {
        List<ItemEventoDTO> eventosFiltrados = new ArrayList<>();
        int cantidadFiltros = tipoFiltrosSeleccionados.size();
        if (cantidadFiltros != 0) {

            if (cantidadFiltros == 1) {
                eventosFiltrados = filtrarPorUno(tipoFiltrosSeleccionados.get(0), valoresFiltrosSeleccionados.get(0));
            }
            if (cantidadFiltros == 2) {
                eventosFiltrados = filtrarPorDos(tipoFiltrosSeleccionados.get(0), tipoFiltrosSeleccionados.get(1), valoresFiltrosSeleccionados.get(0), valoresFiltrosSeleccionados.get(1));
            }
            if (cantidadFiltros == 3) {
                eventosFiltrados = filtrarPorTres(tipoFiltrosSeleccionados.get(0), tipoFiltrosSeleccionados.get(1), tipoFiltrosSeleccionados.get(2), valoresFiltrosSeleccionados.get(0), valoresFiltrosSeleccionados.get(1), valoresFiltrosSeleccionados.get(2));
            }
        }

        return eventosFiltrados;
    }

    // Metodos de filtrado de eventos
    private List<ItemEventoDTO> filtrarPorUno(FiltrosEventos filtro, Enum<?> valor) {
        List<ItemEventoDTO> eventosFiltrados = new ArrayList<>();
        if (filtro == FiltrosEventos.CIUDAD) {
            eventosFiltrados = eventoRepo.findByCiudadEvento(valor.name());
        }
        if (filtro == FiltrosEventos.NOMBRE) {
            eventosFiltrados = eventoRepo.findByNombreEvento(valor.name());
        }
        if (filtro == FiltrosEventos.TIPO) {
            eventosFiltrados = eventoRepo.findByTipoEvento(valor.name());
        }

        return eventosFiltrados;
    }

    private List<ItemEventoDTO> filtrarPorDos(FiltrosEventos filtro1, FiltrosEventos filtro2, Enum<?> valor1, Enum<?> valor2) {
        List<ItemEventoDTO> eventosFiltrados = new ArrayList<>();
        if (filtro1 == FiltrosEventos.NOMBRE && filtro2 == FiltrosEventos.TIPO) {
            eventosFiltrados = eventoRepo.findByNombreEventoAndTipoEvento(valor1.name(), valor2.name());
        }
        if (filtro1 == FiltrosEventos.CIUDAD && filtro2 == FiltrosEventos.TIPO) {
            eventosFiltrados = eventoRepo.findByCiudadEventoAndTipoEvento(valor1.name(), valor2.name());
        }
        if (filtro1 == FiltrosEventos.NOMBRE && filtro2 == FiltrosEventos.TIPO) {
            eventosFiltrados = eventoRepo.findByNombreEventoAndTipoEvento(valor1.name(), valor2.name());
        }
        return eventosFiltrados;
    }

    private List<ItemEventoDTO> filtrarPorTres(FiltrosEventos filtro1, FiltrosEventos filtro2, FiltrosEventos filtro3, Enum<?> valor1, Enum<?> valor2, Enum<?> valor3) {
        List<ItemEventoDTO> eventosFiltrados = new ArrayList<>();
        eventosFiltrados = eventoRepo.findByNombreEventoAndCiudadEventoAndAndTipoEvento(valor1.name(), valor2.name(), valor3.name());
        return eventosFiltrados;
    }
    // Fin metodos de filtrado de eventos
}

// ToDo: Revisar si el metodo de filtrado funciona ^^