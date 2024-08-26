package co.edu.uniquindio.unieventos.model;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class DetalleCarrito {

    private Integer cantidad;
    private String nombreLocalidad;
    private ObjectId idEvento;
}
