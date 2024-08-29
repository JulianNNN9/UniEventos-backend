package co.edu.uniquindio.unieventos.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;


@Document("carritos")
@NoArgsConstructor
@Data
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cupon {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    private String codigo;
    private String nombre;
    private Double porcentajeDescuento;
    private EstadoCupon estadoCupon;
    private TipoCupon tipoCupon;
    private LocalDate fechaVencimiento;
}

enum EstadoCupon { DISPONIBLE, USADO }

enum TipoCupon { UNICO, GENERAL }
