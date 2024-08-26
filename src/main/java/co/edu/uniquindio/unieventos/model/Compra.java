package co.edu.uniquindio.unieventos.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Document("compras")
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Compra {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    private ObjectId idUsuario;
    private List<Item> itemsCompra;
    private Double total;
    private LocalDateTime fecha;
    private ObjectId cupon;
    private Pago pago;
    private String codigoPasarela;
    private EstadoCompra estado;

}

enum EstadoCompra { COMPLETADA, CANCELADA }
