package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.dto.MensajeDTO;
import co.edu.uniquindio.unieventos.dto.TokenDTO;
import co.edu.uniquindio.unieventos.dto.carrito.AgregarItemDTO;
import co.edu.uniquindio.unieventos.dto.carrito.EditarCarritoDTO;
import co.edu.uniquindio.unieventos.dto.carrito.EliminarDelCarritoDTO;
import co.edu.uniquindio.unieventos.dto.carrito.InformacionCarritoDTO;
import co.edu.uniquindio.unieventos.dto.compra.CrearCompraDTO;
import co.edu.uniquindio.unieventos.dto.compra.InformacionCompraDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.*;
import co.edu.uniquindio.unieventos.dto.cupon.InformacionCuponDTO;
import co.edu.uniquindio.unieventos.model.Carrito;
import co.edu.uniquindio.unieventos.model.Compra;
import co.edu.uniquindio.unieventos.model.Cupon;
import co.edu.uniquindio.unieventos.services.interfaces.*;
import com.mercadopago.resources.preference.Preference;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.el.parser.Token;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuario")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class UsuarioControlador {

    private final UsuarioService usuarioService;
    private final CompraService compraService;
    private final CarritoService carritoService;

    @PutMapping("/editar-perfil")
    public ResponseEntity<MensajeDTO<String>> editarUsuario(@Valid @RequestBody EditarUsuarioDTO editarUsuarioDTO)throws Exception{
        usuarioService.editarUsuario(editarUsuarioDTO);
        return ResponseEntity.ok().body( new MensajeDTO<>(false, "Cliente actualizado correctamente") );
    }

    @DeleteMapping("/eliminar-usuario/{codigo}")
    public ResponseEntity<MensajeDTO<String>> eliminarUsuario(@PathVariable String codigo)throws Exception{
        usuarioService.eliminarUsuario(codigo);
        return ResponseEntity.ok().body( new MensajeDTO<>(false, "Cliente eliminado correctamente")
        );
    }

    @GetMapping("/obtener-usuario/{codigo}")
    public ResponseEntity<MensajeDTO<InformacionUsuarioDTO>> obtenerInformacionUsuario(@PathVariable String codigo) throws Exception{
        return ResponseEntity.ok().body( new MensajeDTO<>(false,
                usuarioService.obtenerInformacionUsuario(codigo) ) );
    }

    @PutMapping("/cambiar-contrasenia")
    public ResponseEntity<MensajeDTO<String>> cambiarContrasenia(@RequestBody CambiarContraseniaDTO cambiarContraseniaDTO) throws Exception{
        usuarioService.cambiarContrasenia(cambiarContraseniaDTO);
        return ResponseEntity.ok().body( new MensajeDTO<>(false, "Contraseña cambiada correctamente") );
    }

    @PostMapping("/compra/crear-compra")
    public ResponseEntity<MensajeDTO<String>> crearCompra(@Valid @RequestBody CrearCompraDTO crearCompraDTO) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, compraService.crearCompra(crearCompraDTO)));
    }

    @GetMapping("/compra/obtener-compra/{idCompra}")
    public ResponseEntity<MensajeDTO<InformacionCompraDTO>> obtenerCompra(@PathVariable String idCompra) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, compraService.obtenerCompraDTO(idCompra)));
    }

    //Ver si este metodo retorna la lista
    @GetMapping("/compra/obtener-compras-usuario/{idUsuario}")
    public ResponseEntity<MensajeDTO<List<InformacionCompraDTO>>> obtenerComprasUsuario(@PathVariable String idUsuario) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, compraService.obtenerComprasUsuarioDTO(idUsuario)));
    }

    @GetMapping("/compra/cancelar-compra/{idCompra}")
    public ResponseEntity<MensajeDTO<String>> cancelarCompra(@PathVariable String idCompra) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, compraService.cancelarCompra(idCompra)));
    }

    @GetMapping("/compra/realizar-pago/{idOrden}")
    public ResponseEntity<MensajeDTO<Preference>> realizarPago(@PathVariable String idOrden) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, compraService.realizarPago(idOrden)));
    }

    @PostMapping("/carrito/agregar-carrito")
    public ResponseEntity<MensajeDTO<String>> agregarCarrito(@Valid @RequestBody AgregarItemDTO agregarItemDTO) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, carritoService.agregarAlCarrito(agregarItemDTO)));
    }

    @PostMapping("/carrito/eliminar-carrito")
    public ResponseEntity<MensajeDTO<String>> eliminarCarrito(@Valid @RequestBody EliminarDelCarritoDTO eliminarDelCarritoDTO) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, carritoService.eliminarDelCarrito(eliminarDelCarritoDTO)));
    }

    @PutMapping("/carrito/editar-carrito")
    public ResponseEntity<MensajeDTO<String>> editarCarrito(@Valid @RequestBody EditarCarritoDTO editarCarritoDTO) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, carritoService.editarCarrito(editarCarritoDTO)));
    }

    @GetMapping("/carrito/crear-carrito/{idUsuario}")
    public ResponseEntity<MensajeDTO<String>> crearCarrito(@PathVariable String idUsuario) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, carritoService.crearCarrito(idUsuario)));
    }

    @GetMapping("/carrito/obtener-carrito/{idUsuario}")
    public ResponseEntity<MensajeDTO<InformacionCarritoDTO>> obtenerCarrito(@PathVariable String idUsuario) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, carritoService.obtenerCarritoPorIdUsuarioDTO(idUsuario)));
    }
    @GetMapping("/carrito/validar-cupon/{codigoCupon}/{idUsuario}")
    public ResponseEntity<MensajeDTO<InformacionCuponDTO>> validarCupon(@PathVariable String codigoCupon, @PathVariable String idUsuario) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, compraService.validarYObtenerCupon(codigoCupon, idUsuario)));
    }

    @PostMapping("/carrito/vaciar-carrito/{idUsuario}")
    public ResponseEntity<MensajeDTO<String>> vaciarCarrito(@PathVariable String idUsuario) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, carritoService.vaciarCarrito(idUsuario)));
    }
    // Endpoint para verificar el estado de la compra
    @GetMapping("/{idCompra}/estado")
    public ResponseEntity<MensajeDTO<Map<String, Object>>> verificarEstadoCompra(@PathVariable String idCompra) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, Map.of("compra", compraService.obtenerEstadoCompra(idCompra))));

    }
}