package co.edu.uniquindio.unieventos.services;

import co.edu.uniquindio.unieventos.dto.evento.CrearEventoDTO;
import co.edu.uniquindio.unieventos.dto.evento.EditarEventoDTO;
import co.edu.uniquindio.unieventos.dto.evento.InformacionEventoDTO;
import co.edu.uniquindio.unieventos.dto.evento.ItemEventoDTO;

import java.util.List;

public interface EventoService {

    String crearEvento(CrearEventoDTO crearEventoDTO);

    String editarEvento(EditarEventoDTO editarEventoDTO);

    String eliminarEvento(String id);

    InformacionEventoDTO obtenerInformacionEvento();

    List<ItemEventoDTO> listarEventos();

    List<ItemEventoDTO> filtrarEvento();

}
