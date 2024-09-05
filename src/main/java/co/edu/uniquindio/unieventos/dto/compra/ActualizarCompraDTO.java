package co.edu.uniquindio.unieventos.dto.compra;

import co.edu.uniquindio.unieventos.model.EstadoCompra;
import co.edu.uniquindio.unieventos.model.ItemCompra;
import org.bson.types.ObjectId;
import jakarta.validation.constraints.*;

import java.util.List;

public record ActualizarCompraDTO(
        @NotBlank(message = "El ID de la compra no puede estar vacío")
        String id,

        List<@NotNull(message = "Los items de compra no pueden ser nulos") ItemCompra> itemsCompra,

        @DecimalMin(value = "0.0", inclusive = false, message = "El total debe ser mayor a 0")
        Double total,

        String cupon,

        String pago,

        @Size(max = 100, message = "El código de pasarela no puede tener más de 100 caracteres")
        String codigoPasarela,

        EstadoCompra estado
) {
}
