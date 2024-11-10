package co.edu.uniquindio.unieventos.dto.cupon;

import co.edu.uniquindio.unieventos.model.EstadoCupon;
import co.edu.uniquindio.unieventos.model.TipoCupon;

import java.time.LocalDate;

public record CuponDTO(
        String id,
        String codigo,
        String nombre,
        Double porcentajeDescuento,
        EstadoCupon estadoCupon,
        TipoCupon tipoCupon,
        LocalDate fechaVencimiento
) {}

