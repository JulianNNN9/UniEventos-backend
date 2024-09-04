package co.edu.uniquindio.unieventos.dto.cuenta;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EnviarCodigoAlCorreoDTO(

        @NotBlank(message = "El correo no puede estar vacío")
        @Email(message = "Debe proporcionar un correo electrónico válido")
        String correo,

        @NotBlank(message = "El código no puede estar vacío")
        @Size(max = 20, message = "El código no puede exceder los 20 caracteres")
        String codigo
) {
}
