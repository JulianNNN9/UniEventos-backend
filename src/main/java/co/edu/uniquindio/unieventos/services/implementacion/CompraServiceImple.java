package co.edu.uniquindio.unieventos.services.implementacion;

import co.edu.uniquindio.unieventos.dto.compra.ActualizarCompraDTO;
import co.edu.uniquindio.unieventos.dto.compra.CrearCompraDTO;
import co.edu.uniquindio.unieventos.exceptions.RecursoEncontradoException;
import co.edu.uniquindio.unieventos.exceptions.RecursoNoEncontradoException;
import co.edu.uniquindio.unieventos.model.*;
import co.edu.uniquindio.unieventos.repositories.CompraRepo;
import co.edu.uniquindio.unieventos.services.interfaces.CompraService;
import co.edu.uniquindio.unieventos.services.interfaces.CuponService;
import co.edu.uniquindio.unieventos.services.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CompraServiceImple implements CompraService {

    private final CompraRepo compraRepo;
    private final CuponService cuponService;
    private final UsuarioService usuarioService;

    @Override
    public String crearCompra(CrearCompraDTO crearCompraDTO) throws Exception {

        Cupon cupon = cuponService.obtenerCupon(crearCompraDTO.codigoCupon());

        if(cupon.getTipoCupon() == TipoCupon.UNICO){

            if(cupon.getEstadoCupon() == EstadoCupon.ACTIVO){
                cupon.setEstadoCupon(EstadoCupon.INACTIVO);
            }else{
                throw new Exception("Este cupón ya fue usado por otra persona");
            }

        }else{

            Optional<Compra> compraOptional = compraRepo.findByCodigoCuponAndIdUsuario(crearCompraDTO.codigoCupon(), crearCompraDTO.idUsuario());

            if (compraOptional.isPresent()) {
                throw new RecursoEncontradoException("Este cupon ya ha sido redimido.");
            }

        }

        //TODO Validar entradas restantes

        Usuario usuario = usuarioService.getUsuario(crearCompraDTO.idUsuario());

        Compra compra = Compra.builder()
                .idUsuario(usuario.getId())
                .itemsCompra(crearCompraDTO.itemsCompra())
               // .total() //calcular el total acá
                .fechaCompra(LocalDateTime.now())
                .codigoCupon(crearCompraDTO.codigoCupon())
                .estado(EstadoCompra.PENDIENTE)
                .build();

        compraRepo.save(compra);

        return compra.getId();
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

        //TODO Liberar las entradas (volver a hacerlas disponibles)

        compraRepo.save(compra);

        return "";
    }

    @Override
    public String realizarCompra(String idCompra) {
        //TODO Llamar metodo de la pararela
        return "";
    }
}
