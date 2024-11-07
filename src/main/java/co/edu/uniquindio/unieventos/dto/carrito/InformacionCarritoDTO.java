package co.edu.uniquindio.unieventos.dto.carrito;

import co.edu.uniquindio.unieventos.model.DetalleCarrito;

import java.time.LocalDateTime;
import java.util.List;

public record InformacionCarritoDTO(

        String id,

        LocalDateTime fecha,
        List<InformacionDetalleCarritoDTO>itemsCarrito,
        String idUsuario
){
}
