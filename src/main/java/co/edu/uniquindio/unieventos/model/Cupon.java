package co.edu.uniquindio.unieventos.model;

import lombok.Data;

@Data
public class Cupon {

    private String codigo;
    private String nombre;
    private Double porcentajeDescuento;
    private EstadoCupon estadoCupon;
}

enum EstadoCupon { DISPONIBLE, USADO }
