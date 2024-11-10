package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.services.interfaces.CompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notificaciones")
public class NotificacionControlador {

    private final CompraService compraService;

    @PostMapping("/mercadopago")
    public ResponseEntity<String> recibirNotificacion(@RequestBody Map<String, Object> request) {
        try {
            String resultado = compraService.procesarNotificacionDePago(request);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la notificación: " + e.getMessage());
        }
    }
}