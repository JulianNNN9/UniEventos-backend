package co.edu.uniquindio.unieventos.services.implementacion;


import co.edu.uniquindio.unieventos.dto.evento.CrearEventoDTO;
import co.edu.uniquindio.unieventos.dto.evento.EditarEventoDTO;
import co.edu.uniquindio.unieventos.dto.evento.InformacionEventoDTO;
import co.edu.uniquindio.unieventos.dto.evento.ItemEventoDTO;
import co.edu.uniquindio.unieventos.repositories.EventoRepo;
import co.edu.uniquindio.unieventos.repositories.UsuarioRepo;
import co.edu.uniquindio.unieventos.services.interfaces.EventoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EventoServiceImple implements EventoService {

    private final EventoRepo eventoRepo;
    public EventoServiceImple(EventoRepo eventoRepo) {
        this.eventoRepo = eventoRepo;
    }

    @Override
    public String crearEvento(CrearEventoDTO crearEventoDTO) {
        return "";
    }

    @Override
    public String editarEvento(EditarEventoDTO editarEventoDTO) {
        return "";
    }

    @Override
    public String eliminarEvento(String id) {
        return "";
    }

    @Override
    public InformacionEventoDTO obtenerInformacionEvento() {
        return null;
    }

    @Override
    public List<ItemEventoDTO> listarEventos() {
        return List.of();
    }

    @Override
    public List<ItemEventoDTO> filtrarEvento() {
        return List.of();
    }
}
