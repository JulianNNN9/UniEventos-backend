package co.edu.uniquindio.unieventos.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;

@AllArgsConstructor
@Data
@Builder
public class DetalleCarrito {

    private Integer cantidad;
    private String nombreLocalidad;
    @DBRef
    private Evento evento;
}
