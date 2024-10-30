package co.edu.uniquindio.unieventos.dto.compra;

import co.edu.uniquindio.unieventos.model.ItemCompra;
import jakarta.validation.constraints.*;
import java.util.List;

public record CrearCompraDTO(

        @NotNull(message = "El ID del usuario no puede estar vacío")
        String idUsuario,

        @NotEmpty(message = "La lista de items de compra no puede estar vacía")
        List<@NotNull(message = "Los items de compra no pueden ser nulos") InformacionItemCompraDTO> informacionItemCompraDTOS,

        String codigoCupon
) {
}
