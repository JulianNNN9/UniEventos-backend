package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.dto.MensajeDTO;
import co.edu.uniquindio.unieventos.dto.cupon.CrearEditarCuponDTO;
import co.edu.uniquindio.unieventos.model.Cupon;
import co.edu.uniquindio.unieventos.services.interfaces.CuponService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cupones")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class CuponControlador {

    private final CuponService cuponService;

    @PostMapping ("/crear-cupon")
    public ResponseEntity<MensajeDTO<String>> crearCupon(@Valid @RequestBody CrearEditarCuponDTO crearEditarCuponDTO) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, cuponService.crearCupon(crearEditarCuponDTO)));
    }

    @PutMapping ("/editar-cupon")
    public ResponseEntity<MensajeDTO<String>> editarCupon(@Valid @RequestBody CrearEditarCuponDTO crearEditarCuponDTO) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, cuponService.editarCupon(crearEditarCuponDTO)));
    }

    @GetMapping ("/eliminar-cupon/{idCupon}")
    public ResponseEntity<MensajeDTO<String>> eliminarCupon(@PathVariable String idCupon) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, cuponService.eliminarCupon(idCupon)));
    }

    @GetMapping ("/obtener-cupon/{idCupon}")
    public ResponseEntity<MensajeDTO<Cupon>> obtenerCupon(@PathVariable String idCupon) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, cuponService.obtenerCupon(idCupon)));
    }

}
