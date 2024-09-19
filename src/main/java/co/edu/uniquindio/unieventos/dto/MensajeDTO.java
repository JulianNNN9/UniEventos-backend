package co.edu.uniquindio.unieventos.dto;

public record MensajeDTO<T>(
        boolean error,
        T respuesta
) {
}

