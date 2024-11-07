package co.edu.uniquindio.unieventos.dto.evento;

import co.edu.uniquindio.unieventos.model.Localidad;
import co.edu.uniquindio.unieventos.model.TipoEvento;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public record EliminarEventosDTO(

        List<String> listaIdEventos
) {
}
