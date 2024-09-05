package co.edu.uniquindio.unieventos.dto.compra;

import co.edu.uniquindio.unieventos.model.EstadoCompra;
import co.edu.uniquindio.unieventos.model.ItemCompra;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

public record CrearCompraDTO(

        @NotNull(message = "El ID del usuario no puede estar vacío")
        String idUsuario,

        @NotEmpty(message = "La lista de items de compra no puede estar vacía")
        List<@NotNull(message = "Los items de compra no pueden ser nulos") ItemCompra> itemsCompra,

        @DecimalMin(value = "0.0", inclusive = false, message = "El total debe ser mayor a 0")
        @NotNull(message = "El total no puede estar vacío")
        Double total,

        @NotNull(message = "La fecha de compra no puede estar vacía")
        LocalDateTime fechaCompra,

        String cupon,

        @NotNull(message = "El ID del pago no puede estar vacío")
        String pago,

        @NotBlank(message = "El código de pasarela no puede estar vacío")
        @Size(max = 100, message = "El código de pasarela no puede tener más de 100 caracteres")
        String codigoPasarela,

        @NotNull(message = "El estado de la compra no puede estar vacío")
        EstadoCompra estado
) {
}
