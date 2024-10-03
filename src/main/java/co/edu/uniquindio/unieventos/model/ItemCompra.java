package co.edu.uniquindio.unieventos.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@AllArgsConstructor
@Data
@Builder
public class ItemCompra {

    private String idEvento;
    private String nombreLocalidad;
    private Integer cantidad;
    private Double precioUnitario;

}
