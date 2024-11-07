package co.edu.uniquindio.unieventos.dto.evento;


public record NotificacionEventoDTO(

        String _id,
        String nombreEvento,
        String fechaEvento,
        String descripcionEvento,
        String ciudadEvento,
        String imagenPortada
) {
}
