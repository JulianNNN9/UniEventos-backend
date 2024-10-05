package co.edu.uniquindio.unieventos.services.implementacion;

import co.edu.uniquindio.unieventos.dto.cupon.*;
import co.edu.uniquindio.unieventos.exceptions.RecursoEncontradoException;
import co.edu.uniquindio.unieventos.exceptions.RecursoNoEncontradoException;
import co.edu.uniquindio.unieventos.model.Cupon;
import co.edu.uniquindio.unieventos.model.EstadoCupon;
import co.edu.uniquindio.unieventos.repositories.CuponRepo;
import co.edu.uniquindio.unieventos.services.interfaces.CuponService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CuponServiceImple implements CuponService {

    private final CuponRepo cuponRepo;

    @Override
    public String crearCupon(CrearEditarCuponDTO crearCuponDTO) throws Exception {

        Optional<Cupon> cuponExistente = cuponRepo.findByCodigo(crearCuponDTO.codigo());

        if (cuponExistente.isPresent()) {
            throw new RecursoEncontradoException("El cupon ya existe.");
        }

        Cupon cupon = Cupon.builder()
                .codigo(crearCuponDTO.codigo())
                .nombre(crearCuponDTO.nombre())
                .porcentajeDescuento(crearCuponDTO.porcentajeDescuento())
                .estadoCupon(crearCuponDTO.estadoCupon())
                .tipoCupon(crearCuponDTO.tipoCupon())
                .fechaVencimiento(crearCuponDTO.fechaVencimiento())
                .build();

        cuponRepo.save(cupon);

        return "Cupon creado exitosamente.";
    }

    @Override
    public String editarCupon(CrearEditarCuponDTO crearCuponDTO) throws Exception {

        Cupon cupon = obtenerCuponPorId(crearCuponDTO.id());

        cupon.setCodigo(crearCuponDTO.codigo());
        cupon.setNombre(crearCuponDTO.nombre());
        cupon.setPorcentajeDescuento(crearCuponDTO.porcentajeDescuento());
        cupon.setEstadoCupon(crearCuponDTO.estadoCupon());
        cupon.setTipoCupon(crearCuponDTO.tipoCupon());
        cupon.setFechaVencimiento(crearCuponDTO.fechaVencimiento());

        cuponRepo.save(cupon);

        return cupon.getId();
    }

    private Cupon obtenerCuponPorId(String id) throws RecursoNoEncontradoException {

        Optional<Cupon> cuponExistente = cuponRepo.findById(id);

        if (cuponExistente.isEmpty()) {
            throw new RecursoNoEncontradoException("Cupón no encontrado con el ID: " + id);
        }

        return cuponExistente.get();
    }

    @Override
    public String eliminarCupon(String idCupon) throws Exception {

        Cupon cupon = obtenerCupon(idCupon);

        cupon.setEstadoCupon(EstadoCupon.ELIMINADO);

        cuponRepo.save(cupon);

        return "Cupón eliminado con éxito.";
    }

    @Override
    public Cupon obtenerCupon(String codigo) throws Exception {

        Optional<Cupon> cuponExistente = cuponRepo.findByCodigo(codigo);

        if (cuponExistente.isEmpty()) {
            throw new Exception("Cupón no encontrado con el código: " + codigo);
        }

        return cuponExistente.get();
    }

}
