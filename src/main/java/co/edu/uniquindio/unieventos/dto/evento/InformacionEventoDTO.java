package co.edu.uniquindio.unieventos.dto.evento;

import co.edu.uniquindio.unieventos.model.EstadoEvento;
import co.edu.uniquindio.unieventos.model.Localidad;
import java.time.LocalDateTime;
import java.util.List;

public record InformacionEventoDTO(

        String nombreEvento,
        String direccionEvento,
        String ciudadEvento,
        String descripcionEvento,
        LocalDateTime fechaEvento,
        List<Localidad> localidades,
        EstadoEvento estadoEvento
) {
}
