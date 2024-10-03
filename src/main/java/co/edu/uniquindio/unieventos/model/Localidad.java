package co.edu.uniquindio.unieventos.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@Data
@Builder
public class Localidad {

    private String nombreLocalidad;
    private Double precioLocalidad;
    private Integer capacidadMaxima;
    private Integer entradasRestantes;
}
