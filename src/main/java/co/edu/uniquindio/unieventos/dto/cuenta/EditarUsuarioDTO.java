package co.edu.uniquindio.unieventos.dto.cuenta;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record EditarUsuarioDTO(

        @NotBlank @Length(max = 30) String nombreCompleto,
        @Length(max = 50) String direccion,
        @Length(max = 10) String telefono

) {
}
