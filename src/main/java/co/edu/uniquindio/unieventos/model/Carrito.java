package co.edu.uniquindio.unieventos.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Document("carritos")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Carrito {
    @Id
    @EqualsAndHashCode.Include
    private String id;
    private LocalDateTime fecha;
    private List<DetalleCarrito> itemsCarrito;
    private ObjectId idUsuario;

}
