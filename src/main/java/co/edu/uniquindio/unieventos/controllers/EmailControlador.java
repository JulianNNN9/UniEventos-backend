package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.dto.EmailDTO;
import co.edu.uniquindio.unieventos.dto.MensajeDTO;
import co.edu.uniquindio.unieventos.services.interfaces.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailControlador {

    private final EmailService emailService;

    @PostMapping("/enviar-email")
    public ResponseEntity<MensajeDTO<String>> enviarEmail(EmailDTO emailDTO) throws Exception {
        emailService.enviarCorreo(emailDTO);
        return ResponseEntity.ok().body(new MensajeDTO<>(false, "Email enviado correctamente"));
    }

}
