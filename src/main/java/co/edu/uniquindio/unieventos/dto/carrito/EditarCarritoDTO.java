package co.edu.uniquindio.unieventos.dto.carrito;

import co.edu.uniquindio.unieventos.model.DetalleCarrito;

public record EditarCarritoDTO(

        String idCarrito,

        String nombreLocalidad,
        String idEvento,

        Integer cantidadActualizada
) {
}
