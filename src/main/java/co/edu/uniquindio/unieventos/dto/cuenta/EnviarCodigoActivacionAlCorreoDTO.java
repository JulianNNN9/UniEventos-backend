package co.edu.uniquindio.unieventos.dto.cuenta;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EnviarCodigoActivacionAlCorreoDTO(

        @NotBlank(message = "El correo no puede estar vacío")
        @Email(message = "Debe proporcionar un correo electrónico válido")
        String correo

) {
}
