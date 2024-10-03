package co.edu.uniquindio.unieventos.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@Builder
public class CodigoValidacion {

    private String codigo;
    private LocalDateTime fechaCreacion;
}

