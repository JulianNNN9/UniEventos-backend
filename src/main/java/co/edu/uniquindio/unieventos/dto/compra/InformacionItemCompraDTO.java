package co.edu.uniquindio.unieventos.dto.compra;

import co.edu.uniquindio.unieventos.model.Evento;
import co.edu.uniquindio.unieventos.model.ItemCompra;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

public record InformacionItemCompraDTO(

        String idEvento,
        String nombreLocalidad,
        Integer cantidad,
        Double precioUnitario
) {
}
