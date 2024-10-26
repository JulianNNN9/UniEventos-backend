package co.edu.uniquindio.unieventos.services.implementacion;

import co.edu.uniquindio.unieventos.dto.cupon.*;
import co.edu.uniquindio.unieventos.exceptions.RecursoEncontradoException;
import co.edu.uniquindio.unieventos.exceptions.RecursoNoEncontradoException;
import co.edu.uniquindio.unieventos.model.Cupon;
import co.edu.uniquindio.unieventos.model.EstadoCupon;
import co.edu.uniquindio.unieventos.repositories.CuponRepo;
import co.edu.uniquindio.unieventos.services.interfaces.CuponService;
import co.edu.uniquindio.unieventos.utils.TextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class CuponServiceImple implements CuponService {

    private final CuponRepo cuponRepo;

    @Override
    public String crearCupon(CrearCuponDTO crearCuponDTO) throws RecursoEncontradoException {

        Optional<Cupon> cuponExistente = cuponRepo.findByCodigoAndEstadoNot(crearCuponDTO.codigo(), EstadoCupon.ELIMINADO);

        if (cuponExistente.isPresent()) {
            throw new RecursoEncontradoException("El cupon ya existe");
        }

        Cupon cupon = Cupon.builder()
                .codigo(TextUtils.normalizarTexto(crearCuponDTO.codigo()))
                .nombre(TextUtils.normalizarTexto(crearCuponDTO.nombre()))
                .porcentajeDescuento(crearCuponDTO.porcentajeDescuento())
                .estadoCupon(crearCuponDTO.estadoCupon())
                .tipoCupon(crearCuponDTO.tipoCupon())
                .fechaVencimiento(crearCuponDTO.fechaVencimiento())
                .build();

        cuponRepo.save(cupon);

        return "Cupon creado exitosamente";
    }

    @Override
    public String editarCupon(EditarCuponDTO editarCuponDTO) throws RecursoNoEncontradoException {

        Cupon cupon = obtenerCuponPorId(editarCuponDTO.id());

        cupon.setCodigo(editarCuponDTO.codigo());
        cupon.setNombre(editarCuponDTO.nombre());
        cupon.setPorcentajeDescuento(editarCuponDTO.porcentajeDescuento());
        cupon.setEstadoCupon(editarCuponDTO.estadoCupon());
        cupon.setTipoCupon(editarCuponDTO.tipoCupon());
        cupon.setFechaVencimiento(editarCuponDTO.fechaVencimiento());

        cuponRepo.save(cupon);

        return cupon.getId();
    }
    @Override
    public Cupon obtenerCuponPorId(String id) throws RecursoNoEncontradoException {

        Optional<Cupon> cuponExistente = cuponRepo.findByIdAndEstadoNot(id, EstadoCupon.ELIMINADO);

        if (cuponExistente.isEmpty()) {
            throw new RecursoNoEncontradoException("Cupón no encontrado");
        }

        return cuponExistente.get();
    }

    @Override
    public String eliminarCupon(String idCupon) throws RecursoNoEncontradoException {

        Cupon cupon = obtenerCuponPorId(idCupon);

        cupon.setEstadoCupon(EstadoCupon.ELIMINADO);

        cuponRepo.save(cupon);

        return "Cupón eliminado con éxito.";
    }

    @Override
    public Cupon obtenerCuponPorCodigo(String codigo) throws RecursoNoEncontradoException {

        Optional<Cupon> cuponExistente = cuponRepo.findByCodigoAndEstadoNot(codigo, EstadoCupon.ELIMINADO);

        if (cuponExistente.isEmpty()) {
            throw new RecursoNoEncontradoException("Cupón no encontrado");
        }

        return cuponExistente.get();
    }

    @Override
    public String generarCodigoCupon(){

        String cadena = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        StringBuilder codigo = new StringBuilder();

        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            codigo.append(cadena.charAt(random.nextInt(cadena.length())));
        }

        return codigo.toString();
    }
}
