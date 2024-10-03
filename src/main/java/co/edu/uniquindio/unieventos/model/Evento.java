package co.edu.uniquindio.unieventos.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Document("eventos")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Evento {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    private String nombreEvento;
    private String direccionEvento;
    private String ciudadEvento;
    private String descripcionEvento;
    private TipoEvento tipoEvento;
    private LocalDateTime fechaEvento;
    private LocalDate fechaCreacion;
    private List<Localidad> localidades;
    private String imagenPortada;
    private String imagenLocalidades;
    private EstadoEvento estadoEvento;

    public Localidad obtenerLocalidad(String nombreLocalidad) {
        return localidades.stream()
                .filter(localidad -> localidad.getNombreLocalidad().equals(nombreLocalidad))
                .findFirst()
                .orElse(null);
    }
}

