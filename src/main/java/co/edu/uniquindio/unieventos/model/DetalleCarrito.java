package co.edu.uniquindio.unieventos.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@Data
@SuperBuilder
public class DetalleCarrito {

    private Integer cantidad;
    private String nombreLocalidad;
    private ObjectId idEvento;
}
