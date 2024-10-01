package co.edu.uniquindio.unieventos.dto;

import co.edu.uniquindio.unieventos.model.TipoEvento;

public record FiltrosEventosDTO(
        String nombreEvento,
        TipoEvento tipoEvento,
        String ciudad
) {
}
