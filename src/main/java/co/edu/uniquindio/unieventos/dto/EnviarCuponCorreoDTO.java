package co.edu.uniquindio.unieventos.dto;

public record EnviarCuponCorreoDTO(
        String subjectCorreo,
        String subjectTemplate,
        String couponName,
        String couponCode
){
}
