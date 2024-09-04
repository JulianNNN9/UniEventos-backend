package co.edu.uniquindio.unieventos.services.implementacion;

import co.edu.uniquindio.unieventos.dto.compra.ActualizarCompraDTO;
import co.edu.uniquindio.unieventos.dto.compra.CrearCompraDTO;
import co.edu.uniquindio.unieventos.dto.compra.FiltrarComprasDTO;
import co.edu.uniquindio.unieventos.model.Compra;
import co.edu.uniquindio.unieventos.repositories.CompraRepo;
import co.edu.uniquindio.unieventos.services.interfaces.CompraService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class CompraServiceImple implements CompraService {

    private final CompraRepo compraRepo;

    public CompraServiceImple(CompraRepo compraRepo) {
        this.compraRepo = compraRepo;
    }

    @Override
    public String crearCompra(CrearCompraDTO crearCompraDTO) {
        Compra compra = Compra.builder()
                .idUsuario(crearCompraDTO.idUsuario())
                .itemsCompra(crearCompraDTO.itemsCompra())
                .total(crearCompraDTO.total())
                .fechaCompra(crearCompraDTO.fechaCompra())
                .cupon(crearCompraDTO.cupon())
                .pago(crearCompraDTO.pago())
                .codigoPasarela(crearCompraDTO.codigoPasarela())
                .estado(crearCompraDTO.estado())
                .build();
        compraRepo.save(compra);
        return compra.getId();
    }

    @Override
    public String actualizarCompra(ActualizarCompraDTO actualizarCompraDTO) {
        Optional<Compra> compraExistente = compraRepo.findById(actualizarCompraDTO.id());
        if (compraExistente.isPresent()) {
            Compra compra = compraExistente.get();
            if (actualizarCompraDTO.itemsCompra() != null) {
                compra.setItemsCompra(actualizarCompraDTO.itemsCompra());
            }
            if (actualizarCompraDTO.total() != null) {
                compra.setTotal(actualizarCompraDTO.total());
            }
            if (actualizarCompraDTO.cupon() != null) {
                compra.setCupon(actualizarCompraDTO.cupon());
            }
            if (actualizarCompraDTO.pago() != null) {
                compra.setPago(actualizarCompraDTO.pago());
            }
            if (actualizarCompraDTO.codigoPasarela() != null) {
                compra.setCodigoPasarela(actualizarCompraDTO.codigoPasarela());
            }
            if (actualizarCompraDTO.estado() != null) {
                compra.setEstado(actualizarCompraDTO.estado());
            }
            compraRepo.save(compra);
            return compra.getId();
        } else {
            throw new NoSuchElementException("Compra no encontrada con el ID: " + actualizarCompraDTO.id());
        }
    }

    @Override
    public Compra obtenerCompra(String idCompra) {
        return compraRepo.findById(idCompra)
                .orElseThrow(() -> new NoSuchElementException("Compra no encontrada con el ID: " + idCompra));
    }

    @Override
    public List<Compra> filtrarCompras(FiltrarComprasDTO filtrarComprasDTO) {
        return null;
    }
}
