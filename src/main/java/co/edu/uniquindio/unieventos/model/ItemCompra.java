package co.edu.uniquindio.unieventos.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@Data
@SuperBuilder
public class ItemCompra {

    private ObjectId idEvento;
    private String nombreLocalidad;
    private Integer cantidad;
    private Double precioUnitario;

}
