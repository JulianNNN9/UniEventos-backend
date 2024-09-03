package co.edu.uniquindio.unieventos.dto.carrito;

import co.edu.uniquindio.unieventos.model.DetalleCarrito;

public record AgregarItemDTO (

        String idCarrito,
        DetalleCarrito detalleCarrito
){
}
