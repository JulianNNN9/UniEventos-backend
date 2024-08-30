package co.edu.uniquindio.unieventos.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document("compras")
@NoArgsConstructor
@Data
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Compra {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    private ObjectId idUsuario;
    private List<ItemCompra> itemsCompra;
    private Double total;
    private LocalDate fechaCompra;
    private ObjectId cupon;
    private ObjectId pago;
    private String codigoPasarela;
    private EstadoCompra estado;

}

enum EstadoCompra { COMPLETADA, CANCELADA }
