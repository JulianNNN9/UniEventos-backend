package co.edu.uniquindio.unieventos.model;

import lombok.Data;

@Data
public class Localidad {

    private String nombre;
    private Double precio;
    private Integer capacidadMaxima;
    private Integer entradasVendidas;
}
