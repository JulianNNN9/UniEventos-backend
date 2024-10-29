package co.edu.uniquindio.unieventos.dto.compra;

import co.edu.uniquindio.unieventos.model.EstadoCompra;
import co.edu.uniquindio.unieventos.model.ItemCompra;
import co.edu.uniquindio.unieventos.model.Pago;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record InformacionCompraDTO(

        String id,

        String idUsuario,
        List<ItemCompra> itemsCompra,
        Double total,
        LocalDateTime fechaCompra,
        String codigoCupon,
        EstadoCompra estadoCompra,
        String codigoPasarela,
        Pago pago
) {
}
