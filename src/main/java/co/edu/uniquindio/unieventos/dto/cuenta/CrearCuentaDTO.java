package co.edu.uniquindio.unieventos.dto.cuenta;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CrearCuentaDTO(

    @NotBlank @Length(max = 10) String cedula,
    @NotBlank @Length(max = 25) String nombreCompleto,
    @Length(max = 10) String direccion,
    @Length(max = 10) String telefono,
    @NotBlank @Length(max = 50) @Email String email,
    @NotBlank @Length(min = 6 ) @Length(max = 20) String contrasenia

){
}
