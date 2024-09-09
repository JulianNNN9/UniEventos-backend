package co.edu.uniquindio.unieventos.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class Localidad {

    private String nombreLocalidad;
    private Double precioLocalidad;
    private Integer capacidadMaxima;
    private Integer entradasRestantes;
}
