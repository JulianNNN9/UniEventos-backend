package co.edu.uniquindio.unieventos.dto.cupon;

import co.edu.uniquindio.unieventos.model.EstadoCupon;
import co.edu.uniquindio.unieventos.model.TipoCupon;
import co.edu.uniquindio.unieventos.model.Usuario;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CrearCuponDTO (

        @NotBlank(message = "El código no puede estar vacío")
        @Size(max = 20, message = "El código no puede exceder los 20 caracteres")
        String codigo,

        @NotBlank(message = "El nombre no puede estar vacío")
        @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
        String nombre,

        @NotNull(message = "El porcentaje de descuento es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false, message = "El porcentaje de descuento debe ser mayor que 0")
        @DecimalMax(value = "100.0", message = "El porcentaje de descuento no puede ser mayor a 100")
        Double porcentajeDescuento,

        @NotNull(message = "El tipo de cupón es obligatorio")
        TipoCupon tipoCupon,

        @NotNull(message = "La fecha de vencimiento es obligatoria")
        @Future(message = "La fecha de vencimiento debe estar en el futuro")
        LocalDate fechaVencimiento,
        Usuario usuario
){
}
