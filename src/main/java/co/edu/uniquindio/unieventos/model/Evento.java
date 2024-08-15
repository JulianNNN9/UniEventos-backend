package co.edu.uniquindio.unieventos.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Document("eventos")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class Evento {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    private String nombre;
    private String direccion;
    private String ciudad;
    private String descripcion;
    private TipoEvento tipoEvento;
    private LocalDateTime fecha;
    private List<Localidad> localidades;
}

enum TipoEvento { CONCIERTO, TEATRO, DEPORTE, FESTIVAL, OTRO }
