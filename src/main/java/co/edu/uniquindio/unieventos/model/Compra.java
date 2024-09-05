package co.edu.uniquindio.unieventos.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private String idUsuario;
    private List<ItemCompra> itemsCompra;
    private Double total;
    private LocalDateTime fechaCompra;
    private String cupon;
    private String pago;
    private String codigoPasarela;
    private EstadoCompra estado;

}

