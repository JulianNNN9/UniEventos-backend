package co.edu.uniquindio.unieventos.services.implementacion;

import co.edu.uniquindio.unieventos.dto.cupon.*;
import co.edu.uniquindio.unieventos.exceptions.RecursoEncontradoException;
import co.edu.uniquindio.unieventos.model.Cupon;
import co.edu.uniquindio.unieventos.model.EstadoCupon;
import co.edu.uniquindio.unieventos.repositories.CuponRepo;
import co.edu.uniquindio.unieventos.services.interfaces.CuponService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
public class CuponServiceImple implements CuponService {

    private final CuponRepo cuponRepo;

    public CuponServiceImple(CuponRepo cuponRepo) {
        this.cuponRepo = cuponRepo;
    }

    @Override
    public String crearCupon(Crear_EditarCuponDTO crearCuponDTO) throws Exception {

        Optional<Cupon> cuponExistente = cuponRepo.findByCodigo(crearCuponDTO.codigo());

        if (cuponExistente.isEmpty()) {
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
    public String editarCupon(Crear_EditarCuponDTO crearCuponDTO) throws Exception {

        Optional<Cupon> cuponExistente = cuponRepo.findById(crearCuponDTO.codigo());

        if (cuponExistente.isEmpty()) {
            throw new Exception("Cupón no encontrado con el ID: " + crearCuponDTO.codigo());
        }

        Cupon cupon = cuponExistente.get();

        cupon.setCodigo(crearCuponDTO.codigo());
        cupon.setNombre(crearCuponDTO.nombre());
        cupon.setPorcentajeDescuento(crearCuponDTO.porcentajeDescuento());
        cupon.setEstadoCupon(crearCuponDTO.estadoCupon());
        cupon.setTipoCupon(crearCuponDTO.tipoCupon());
        cupon.setFechaVencimiento(crearCuponDTO.fechaVencimiento());

        cuponRepo.save(cupon);

        return cupon.getId();
    }

    @Override
    public String eliminarCupon(String idCupon) throws Exception {

        if (!cuponRepo.existsById(idCupon)) {
            throw new Exception("Cupón no encontrado con el ID: " + idCupon);
        }

        cuponRepo.deleteById(idCupon);

        return "Cupón eliminado con éxito.";
    }
    

}
