package co.edu.uniquindio.unieventos.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@Data
@Builder
public class DetalleCarrito {

    private Integer cantidad;
    private String nombreLocalidad;
    private String idEvento;
}
