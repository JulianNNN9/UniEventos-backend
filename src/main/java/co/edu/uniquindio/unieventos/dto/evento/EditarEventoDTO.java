package co.edu.uniquindio.unieventos.dto.evento;

import co.edu.uniquindio.unieventos.model.EstadoEvento;
import co.edu.uniquindio.unieventos.model.Localidad;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

public record EditarEventoDTO(

        String idEvento,

        @NotBlank(message = "El nombre del evento no puede estar vacío")
        @Size(max = 100, message = "El nombre del evento no puede tener más de 100 caracteres")
        String nombreEvento,

        @NotBlank(message = "La dirección del evento no puede estar vacía")
        @Size(max = 200, message = "La dirección del evento no puede tener más de 200 caracteres")
        String direccionEvento,

        @NotBlank(message = "La ciudad del evento no puede estar vacía")
        @Size(max = 100, message = "La ciudad del evento no puede tener más de 100 caracteres")
        String ciudadEvento,

        @NotBlank(message = "La descripción del evento no puede estar vacía")
        @Size(max = 500, message = "La descripción del evento no puede tener más de 500 caracteres")
        String descripcionEvento,

        @FutureOrPresent(message = "La fecha del evento debe ser hoy o en el futuro")
        LocalDateTime fechaEvento,

        @NotEmpty(message = "Debe haber al menos una localidad")
        List<Localidad> localidades,

        @NotBlank(message = "La imagen de portada no puede estar vacía")
        String imagenPortada,

        @NotBlank(message = "La imagen de localidades no puede estar vacía")
        String imagenLocalidades,

        @NotNull(message = "El estado del evento no puede estar vacío")
        EstadoEvento estadoEvento
) {
}
