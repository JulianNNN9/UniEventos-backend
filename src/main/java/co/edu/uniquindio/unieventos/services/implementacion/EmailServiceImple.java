package co.edu.uniquindio.unieventos.services.implementacion;

import co.edu.uniquindio.unieventos.dto.EnviarCodigoCorreoDTO;
import co.edu.uniquindio.unieventos.dto.EnviarCuponCorreoDTO;
import co.edu.uniquindio.unieventos.services.interfaces.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.HashMap;
import java.util.Map;


@Service
public class EmailServiceImple implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailServiceImple(JavaMailSender mailSender, TemplateEngine templateEngine){
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }
    @Override
    @Async
    public void enviarCodigoCorreo(String to, EnviarCodigoCorreoDTO enviarCodigoCorreoDTO) throws MessagingException {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("subject", enviarCodigoCorreoDTO.subjectTemplate());
        templateModel.put("body", enviarCodigoCorreoDTO.body());
        templateModel.put("code", enviarCodigoCorreoDTO.code());

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        Context context = new Context();
        context.setVariables(templateModel);

        String htmlBody = templateEngine.process("emailTemplate", context);

        helper.setTo(to);
        helper.setSubject(enviarCodigoCorreoDTO.subjectCorreo());
        helper.setText(htmlBody, true);

        mailSender.send(message);
    }
    @Override
    @Async
    public void enviarCuponCorreo(String to, EnviarCuponCorreoDTO enviarCuponCorreoDTO) throws MessagingException {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("subject", enviarCuponCorreoDTO.subjectTemplate());
        templateModel.put("couponName", enviarCuponCorreoDTO.couponName());
        templateModel.put("couponCode", enviarCuponCorreoDTO.couponCode());

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        Context context = new Context();
        context.setVariables(templateModel);

        String htmlBody = templateEngine.process("cuponTemplate", context);

        helper.setTo(to);
        helper.setSubject(enviarCuponCorreoDTO.subjectCorreo());
        helper.setText(htmlBody, true);

        mailSender.send(message);
    }
    @Override
    @Async
    public void enviarCorreoSimple(String to, String subject, String emailContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(emailContent, true); // true para interpretar HTML

        mailSender.send(message);
    }

}
