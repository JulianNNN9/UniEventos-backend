package co.edu.uniquindio.unieventos.dto.evento;

import co.edu.uniquindio.unieventos.model.EstadoEvento;

import java.time.LocalDate;

public record ItemEventoDTO(

        String nombreEvento,
        String direccionEvento,
        String ciudadEvento,
        LocalDate fechaEvento,
        EstadoEvento estadoEvento
) {
}
