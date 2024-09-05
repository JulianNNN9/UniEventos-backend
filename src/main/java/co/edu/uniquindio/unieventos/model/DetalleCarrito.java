package co.edu.uniquindio.unieventos.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class DetalleCarrito {

    private Integer cantidad;
    private String nombreLocalidad;
    private String idEvento;
}
