package co.edu.uniquindio.unieventos.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document("compras")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Compra {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    private String idUsuario;
    private List<ItemCompra> itemsCompra;
    private Double total;
    private LocalDateTime fechaCompra;
    private String codigoCupon;
    private EstadoCompra estadoCompra;
    private String codigoPasarela;
    private Pago pago;

}

