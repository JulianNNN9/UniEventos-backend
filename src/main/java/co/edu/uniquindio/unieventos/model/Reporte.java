package co.edu.uniquindio.unieventos.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("reportes")
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class Reporte {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    private ObjectId idEvento;
    private Double porcentajeVendidoPorLocalidad;
    private Double totalGanado;
}
