package co.edu.uniquindio.unieventos.dto.carrito;

import org.bson.types.ObjectId;

public record AgregarAlCarritoDTO(

        Integer cantidad,
        String nombreLocalidad,
        ObjectId idEvento
) {
}
