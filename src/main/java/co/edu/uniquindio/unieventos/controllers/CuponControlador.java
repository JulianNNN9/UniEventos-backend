package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.dto.MensajeDTO;
import co.edu.uniquindio.unieventos.dto.cupon.CrearEditarCuponDTO;
import co.edu.uniquindio.unieventos.model.Cupon;
import co.edu.uniquindio.unieventos.services.interfaces.CuponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cupones")
@RequiredArgsConstructor
public class CuponControlador {

    private final CuponService cuponService;

    @PostMapping ("/crear-cupon")
    public ResponseEntity<MensajeDTO<String>> crearCupon(@Valid @RequestBody CrearEditarCuponDTO crearEditarCuponDTO) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, cuponService.crearCupon(crearEditarCuponDTO)));
    }

    @PostMapping ("/editar-cupon")
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
