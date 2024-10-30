package co.edu.uniquindio.unieventos.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;

@AllArgsConstructor
@Data
@Builder
public class ItemCompra {

    @DBRef
    private Evento evento;
    private String nombreLocalidad;
    private Integer cantidad;
    private Double precioUnitario;

}
