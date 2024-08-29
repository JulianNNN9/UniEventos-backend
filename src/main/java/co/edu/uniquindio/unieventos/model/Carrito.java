package co.edu.uniquindio.unieventos.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.util.List;

@Document("carritos")
@NoArgsConstructor
@Data
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Carrito {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    private LocalDate fecha;
    private List<DetalleCarrito> itemsCarrito;
    private ObjectId idUsuario;

}