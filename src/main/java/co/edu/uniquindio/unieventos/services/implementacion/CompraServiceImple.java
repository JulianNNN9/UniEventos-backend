package co.edu.uniquindio.unieventos.services.implementacion;

import co.edu.uniquindio.unieventos.dto.compra.ActualizarCompraDTO;
import co.edu.uniquindio.unieventos.dto.compra.CrearCompraDTO;
import co.edu.uniquindio.unieventos.exceptions.RecursoNoEncontradoException;
import co.edu.uniquindio.unieventos.model.Compra;
import co.edu.uniquindio.unieventos.model.EstadoCompra;
import co.edu.uniquindio.unieventos.repositories.CompraRepo;
import co.edu.uniquindio.unieventos.services.interfaces.CompraService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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

        //VALIDAR QUE EL CUPON A REDIMIR NO HAYA SIOD USANDO ANTES POR EL USUARIO
        //SI ES DE UNICO USO, CMBIAR EL ESTADO DEL CUPÓN A INACTIVO
        //validar crearCompraDTO.idUsuario()

        Compra compra = Compra.builder()
                .idUsuario(crearCompraDTO.idUsuario())
                .itemsCompra(crearCompraDTO.itemsCompra())
               // .total() //calcular el total acá
                .fechaCompra(LocalDateTime.now())
                .cupon(crearCompraDTO.cupon())
                .estado(EstadoCompra.PENDIENTE)
                .build();

        compraRepo.save(compra);

        return compra.getId();
    }

    @Override
    public String actualizarCompra(ActualizarCompraDTO actualizarCompraDTO) throws Exception {

        Optional<Compra> compraExistente = compraRepo.findById(actualizarCompraDTO.id());

        if (compraExistente.isPresent()) {

            Compra compra = compraExistente.get();

            if (actualizarCompraDTO.itemsCompra() != null) {
                compra.setItemsCompra(actualizarCompraDTO.itemsCompra());
            }
            if (actualizarCompraDTO.total() != null) {
                compra.setTotal(actualizarCompraDTO.total());
            }
            if (actualizarCompraDTO.idCupon() != null) {
                compra.setCupon(actualizarCompraDTO.idCupon());
            }
            if (actualizarCompraDTO.idPago() != null) {
                compra.setPago(actualizarCompraDTO.idPago());
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
            throw new RecursoNoEncontradoException("Compra no encontrada con el ID: " + actualizarCompraDTO.id());
        }
    }

    @Override
    public Compra obtenerCompra(String idCompra) throws Exception {

        Optional<Compra> compraExistente = compraRepo.findById(idCompra);

        if (compraExistente.isEmpty()) {
            throw new RecursoNoEncontradoException("Compra no encontrada con el ID: " + idCompra);
        }

        return compraExistente.get();
    }

    @Override
    public List<Compra> obtenerComprasUsuario(String idUsuario) throws Exception{

        List<Compra> comprasExistente = compraRepo.findAllByIdUsuario(idUsuario);

        if (comprasExistente.isEmpty()) {
            throw new RecursoNoEncontradoException("Usuario no encontrado con el ID: " + idUsuario);
        }

        return comprasExistente;
    }

    @Override
    public String cancelarCompra(String idCompra) throws Exception {

        Optional<Compra> compraExistente = compraRepo.findById(idCompra);

        if (compraExistente.isEmpty()) {
            throw new RecursoNoEncontradoException("Compra no encontrado con el ID: " + idCompra);
        }

        Compra compra = compraExistente.get();

        compra.setEstado(EstadoCompra.CANCELADA);

        //Liberar las entradas (volver a hacerlas disponibles)

        compraRepo.save(compra);

        return "";
    }

    @Override
    public String realizarCompra(String idCompra) {
        //TODO Llamar metodo de la pararela
        return "";
    }
}
