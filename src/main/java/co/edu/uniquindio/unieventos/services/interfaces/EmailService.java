package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.EnviarCodigoCorreoDTO;
import co.edu.uniquindio.unieventos.dto.EnviarCuponCorreoDTO;
import jakarta.mail.MessagingException;

public interface EmailService {

    void enviarCodigoCorreo(String to, EnviarCodigoCorreoDTO enviarCodigoCorreoDTO) throws MessagingException;
    void enviarCuponCorreo(String to, EnviarCuponCorreoDTO enviarCuponCorreoDTO) throws MessagingException;
    void enviarCorreoSimple(String to, String subject, String emailContent) throws MessagingException;
}
