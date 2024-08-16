package co.edu.uniquindio.unieventos.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Pago {

    //Implementar pasarela de pago
    private MetodoPago metodoPago;
    private LocalDateTime fecha;
    private EstadoPago estado;
    private Double monto;
}

enum MetodoPago { EFECTIVO, CREDITO, DEBITO }

enum EstadoPago { CANCELADO, PENDIENTE, FINALIZADO }
