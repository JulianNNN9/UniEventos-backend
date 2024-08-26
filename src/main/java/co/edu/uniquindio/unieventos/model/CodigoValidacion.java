package co.edu.uniquindio.unieventos.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CodigoValidacion {

    private LocalDate fechaCreacion;
    private String codigo;
}
