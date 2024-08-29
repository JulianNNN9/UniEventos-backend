package co.edu.uniquindio.unieventos.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
public class CodigoValidacion {

    private LocalDate fechaCreacion;
    private String codigo;
}
