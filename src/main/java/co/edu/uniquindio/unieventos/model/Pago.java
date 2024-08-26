package co.edu.uniquindio.unieventos.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("pagos")
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pago {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    //Implementar pasarela de pago
    private MetodoPago metodoPago;
    private LocalDateTime fecha;
    private String estado;
    private Double monto;
}

enum MetodoPago { EFECTIVO, CREDITO, DEBITO }
