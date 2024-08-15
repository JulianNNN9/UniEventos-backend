package co.edu.uniquindio.unieventos.model;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Item {

    private ObjectId idEvento;
    private ObjectId idLocalidad;
    private int cantidad;
    private double precioUnitario;

}
