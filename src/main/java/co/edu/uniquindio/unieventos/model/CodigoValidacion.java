package co.edu.uniquindio.unieventos.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@SuperBuilder
public class CodigoValidacion {

    private LocalDateTime fechaCreacion;
    private String codigo;
}
