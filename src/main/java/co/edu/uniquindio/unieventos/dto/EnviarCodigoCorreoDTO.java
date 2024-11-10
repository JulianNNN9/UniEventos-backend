package co.edu.uniquindio.unieventos.dto;

public record EnviarCodigoCorreoDTO(
        String subjectCorreo,
        String subjectTemplate,
        String body,
        String code
){
}
