package co.edu.uniquindio.unieventos.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Document("compras")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class Compra {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    private ObjectId idUsuario;
    private List<Item> itemsCompra;
    private double total;
    private LocalDateTime fecha;
    private Pago pago;
    //Codigo QR
    private EstadoCompra estado;

}

enum EstadoCompra { COMPLETADA, CANCELADA }
