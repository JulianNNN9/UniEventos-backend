package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.dto.ImagenDTO;
import co.edu.uniquindio.unieventos.dto.MensajeDTO;
import co.edu.uniquindio.unieventos.services.interfaces.ImagenesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/imagenes")
@RequiredArgsConstructor
public class ImagenControlador{

    private ImagenesService imagenesService;

    @PostMapping("/subir")
    public ResponseEntity<MensajeDTO<String>> subir(@RequestParam("file") MultipartFile imagen) throws Exception{
        String respuesta = imagenesService.subirImagen(imagen);
        return ResponseEntity.ok().body(new MensajeDTO<>(false, respuesta ));
    }
    @DeleteMapping("/eliminar")
    public ResponseEntity<MensajeDTO<String>> eliminar(@RequestBody ImagenDTO imagenDTO) throws Exception{
        imagenesService.eliminarImagen( imagenDTO.id() );
        return ResponseEntity.ok().body(new MensajeDTO<>(false, "Imagen eliminada correctamente" ));
    }

}