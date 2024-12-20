package co.edu.uniquindio.unieventos.dto.evento;

import java.time.LocalDateTime;

public record ItemEventoDTO(
        String id,
        String nombreEvento,
        String direccionEvento,
        String ciudadEvento,
        LocalDateTime fechaEvento,
        String imagenPortada
) {
}
