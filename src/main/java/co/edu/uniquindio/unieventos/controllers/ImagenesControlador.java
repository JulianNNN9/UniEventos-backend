package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.dto.MensajeDTO;
import co.edu.uniquindio.unieventos.services.interfaces.ImagenesService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/imagenes")
@SecurityRequirement(name = "bearerAuth")
public class ImagenesControlador {

    private final ImagenesService imagenesService;

    @PostMapping("/subir")
    public ResponseEntity<MensajeDTO<String>> subir(@RequestParam("imagen") MultipartFile imagen) throws Exception{
        String respuesta = imagenesService.subirImagen(imagen);
        return ResponseEntity.ok().body(new MensajeDTO<>(false, respuesta));
    }


    @DeleteMapping("/eliminar")
    public ResponseEntity<MensajeDTO<String>> eliminar(@RequestParam("idImagen") String idImagen)  throws Exception{
        imagenesService.eliminarImagen( idImagen );
        return ResponseEntity.ok().body(new MensajeDTO<>(false, "La imagen fue eliminada correctamente"));
    }


}

