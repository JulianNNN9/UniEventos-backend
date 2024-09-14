package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.EmailDTO;

public interface EmailService {

    void enviarCorreo(EmailDTO emailDTO) throws Exception;

}
