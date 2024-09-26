package co.edu.uniquindio.unieventos.controladores;

import co.edu.uniquindio.unieventos.dto.MensajeDTO;
import co.edu.uniquindio.unieventos.dto.compra.CrearCompraDTO;
import co.edu.uniquindio.unieventos.model.Compra;
import co.edu.uniquindio.unieventos.services.interfaces.CompraService;
import com.mercadopago.resources.preference.Preference;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compras")
@RequiredArgsConstructor
public class CompraControlador {

    private final CompraService compraService;

    @PostMapping("/crear-compra")
    public ResponseEntity<MensajeDTO<String>> crearCompra(@Valid @RequestBody CrearCompraDTO crearCompraDTO) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, compraService.crearCompra(crearCompraDTO)));
    }

    @GetMapping("/obtener-compra/{idCompra}")
    public ResponseEntity<MensajeDTO<Compra>> obtenerCompra(@PathVariable String idCompra) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, compraService.obtenerCompra(idCompra)));
    }

    //Ver si este metodo retorna la lista
    @GetMapping("/obtener-compras-usuario/{idUsuario}")
    public ResponseEntity<MensajeDTO<List<Compra>>> obtenerComprasUsuario(@PathVariable String idUsuario) throws Exception {

        return ResponseEntity.ok().body(new MensajeDTO<>(false, compraService.obtenerComprasUsuario(idUsuario)));
    }

    @GetMapping("/cancelar-compra/{idCompra}")
    public ResponseEntity<MensajeDTO<String>> cancelarCompra(@PathVariable String idCompra) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, compraService.cancelarCompra(idCompra)));
    }

    @GetMapping("/realizar-pago/{idOrden}")
    public ResponseEntity<MensajeDTO<Preference>> realizarPago(@PathVariable String idOrden) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, compraService.realizarPago(idOrden)));
    }

    // todo implementar lo de recibir notificacion de mercado pago
}
