package co.edu.uniquindio.unieventos.dto.evento;

import co.edu.uniquindio.unieventos.model.Localidad;
import co.edu.uniquindio.unieventos.model.TipoEvento;

import java.time.LocalDate;
import java.util.List;

public record CrearEventoDTO(

        String nombreEvento,
        String direccionEvento,
        String ciudadEvento,
        String descripcionEvento,
        TipoEvento tipoEvento,
        LocalDate fechaEvento,
        List<Localidad> localidades,
        String imagenPortada,
        String imagenLocalidades
) {
}
