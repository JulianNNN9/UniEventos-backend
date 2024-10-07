package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.evento.*;
import co.edu.uniquindio.unieventos.exceptions.RecursoNoEncontradoException;
import co.edu.uniquindio.unieventos.model.Evento;
import co.edu.uniquindio.unieventos.dto.FiltrosEventosDTO;

import java.util.List;

public interface EventoService {

    String crearEvento(CrearEventoDTO crearEventoDTO) throws Exception;

    String editarEvento(EditarEventoDTO editarEventoDTO) throws Exception;

    String eliminarEvento(String idEvento) throws Exception;

    InformacionEventoDTO obtenerInformacionEvento(String idEvento) throws Exception;

    /* Se usaría cada vez que se cree un nuevo evento
    * idealmente, en los eventos que se creen, se notificará a los usuarios
    */
    List<NotificacionEventoDTO> notificarNuevoEvento() throws Exception;

    List<ItemEventoDTO> filtrarEvento(FiltrosEventosDTO filtrosEventos);

    List<ItemEventoDTO> buscarEventoPorNombre(String nombreEvento);

    List<ItemEventoDTO> listarEventos() throws Exception;

    Evento obtenerEvento(String idEvento) throws RecursoNoEncontradoException;

    void saveEvento(Evento evento) throws Exception;

}
