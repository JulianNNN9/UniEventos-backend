package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.dto.MensajeDTO;
import co.edu.uniquindio.unieventos.dto.carrito.AgregarItemDTO;
import co.edu.uniquindio.unieventos.dto.carrito.EditarCarritoDTO;
import co.edu.uniquindio.unieventos.dto.carrito.EliminarDelCarritoDTO;
import co.edu.uniquindio.unieventos.model.Carrito;
import co.edu.uniquindio.unieventos.services.interfaces.CarritoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class CarritoControlador {

    private final CarritoService carritoService;

    @PostMapping("/agregar-carrito")
    public ResponseEntity<MensajeDTO<String>> agregarCarrito(@Valid @RequestBody AgregarItemDTO agregarItemDTO) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, carritoService.agregarAlCarrito(agregarItemDTO)));
    }

    @PostMapping("/eliminar-carrito")
    public ResponseEntity<MensajeDTO<String>> eliminarCarrito(@Valid @RequestBody EliminarDelCarritoDTO eliminarDelCarritoDTO) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, carritoService.eliminarDelCarrito(eliminarDelCarritoDTO)));
    }

    @PutMapping("/editar-carrito")
    public ResponseEntity<MensajeDTO<String>> editarCarrito(@Valid @RequestBody EditarCarritoDTO editarCarritoDTO) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, carritoService.editarCarrito(editarCarritoDTO)));
    }

    @GetMapping("/crear-carrito/{idUsuario}")
    public ResponseEntity<MensajeDTO<String>> crearCarrito(@PathVariable String idUsuario) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, carritoService.crearCarrito(idUsuario)));
    }

    @GetMapping("/obtener-carrito/{idCarrito}")
    public ResponseEntity<MensajeDTO<Carrito>> obtenerCarrito(@PathVariable String idCarrito) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, carritoService.obtenerCarrito(idCarrito)));
    }
}
