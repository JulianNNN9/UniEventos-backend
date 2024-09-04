package co.edu.uniquindio.unieventos.dto.compra;

import co.edu.uniquindio.unieventos.model.EstadoCompra;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record FiltrarComprasDTO(
        LocalDateTime fechaInicio,
        LocalDateTime fechaFin,
        EstadoCompra estado
) {
}
