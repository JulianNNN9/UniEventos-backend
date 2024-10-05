package co.edu.uniquindio.unieventos.dto;

import co.edu.uniquindio.unieventos.model.TipoEvento;

import javax.annotation.Nullable;

public record FiltrosEventosDTO(
        @Nullable String nombreEvento,
        @Nullable String tipoEvento,
        @Nullable String ciudadEvento
) {
}
