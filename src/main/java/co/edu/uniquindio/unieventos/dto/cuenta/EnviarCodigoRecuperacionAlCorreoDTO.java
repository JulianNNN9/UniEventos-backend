package co.edu.uniquindio.unieventos.dto.cuenta;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EnviarCodigoRecuperacionAlCorreoDTO(

        @NotBlank(message = "El correo no puede estar vacío")
        @Email(message = "Debe proporcionar un correo electrónico válido")
        String correo

) {
}
