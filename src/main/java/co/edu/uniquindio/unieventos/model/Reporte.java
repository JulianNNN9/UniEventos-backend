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

    private Evento idEvento;//Cambiar a objectid
    private Usuario idAdministrador;//Cambiar a objectid
    private Double porcentajeVendidoPorLocalidad;
    private Double totalGanado;
}
