package co.edu.uniquindio.unieventos.dto.cuenta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record ActivarCuentaDTO(
        @NotBlank(message = "El correo no puede estar vacío")
        String email,
        @NotBlank(message = "El código de activación no puede estar vacío")
        String codigoActivacion
) {
}
