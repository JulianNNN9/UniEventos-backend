package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.evento.CrearEventoDTO;
import co.edu.uniquindio.unieventos.dto.evento.EditarEventoDTO;
import co.edu.uniquindio.unieventos.dto.evento.InformacionEventoDTO;
import co.edu.uniquindio.unieventos.dto.evento.ItemEventoDTO;
import co.edu.uniquindio.unieventos.model.Evento;
import co.edu.uniquindio.unieventos.model.FiltrosEventos;

import java.util.List;

public interface EventoService {

    String crearEvento(CrearEventoDTO crearEventoDTO) throws Exception;

    String editarEvento(EditarEventoDTO editarEventoDTO) throws Exception;

    String eliminarEvento(String idEvento) throws Exception;

    InformacionEventoDTO obtenerInformacionEvento(String idEvento) throws Exception;

    List<ItemEventoDTO> filtrarEvento(List<FiltrosEventos> tipoFiltrosSeleccionados, List<Enum<?>> valoresFiltrosSeleccionados);

    List<ItemEventoDTO> buscarEvento(String valorCampoDeBusqueda);

    List<ItemEventoDTO> listarEventos() throws Exception;

    Evento obtenerEvento(String idEvento) throws Exception;

    void saveEvento(Evento evento) throws Exception;

}
