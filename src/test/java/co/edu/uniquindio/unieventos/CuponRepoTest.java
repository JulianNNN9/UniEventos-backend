package co.edu.uniquindio.unieventos;

import co.edu.uniquindio.unieventos.model.Cupon;
import co.edu.uniquindio.unieventos.model.EstadoCupon;
import co.edu.uniquindio.unieventos.model.TipoCupon;
import co.edu.uniquindio.unieventos.model.Usuario;
import co.edu.uniquindio.unieventos.repositories.CuponRepo;
import co.edu.uniquindio.unieventos.repositories.UsuarioRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
class CuponRepoTest {

    @Autowired
    private CuponRepo cuponRepo;

    @Test
    public void agregarCuponTest() {

        Cupon cupon = Cupon.builder()
                .codigo("NOMBRE1CUPON")
                .nombre("Descuento del 1.1.%")
                .porcentajeDescuento(1.1)
                .estadoCupon(EstadoCupon.ACTIVO)
                .tipoCupon(TipoCupon.GENERAL)
                .fechaVencimiento(LocalDate.now().plusDays(10))
                .build();

        Assertions.assertNotNull(cuponRepo.save(cupon));
    }

    @Test
    public void eliminarCuponTest() {
        String nombreCodigo = "CUPONELIMINAR";

        Cupon cupon = Cupon.builder()
                .codigo(nombreCodigo)
                .nombre("Descuento del 1.1.%")
                .porcentajeDescuento(1.1)
                .estadoCupon(EstadoCupon.ACTIVO)
                .tipoCupon(TipoCupon.GENERAL)
                .fechaVencimiento(LocalDate.now().plusDays(10))
                .build();

        cuponRepo.save(cupon);
        Optional<Cupon> cuponNuevo = cuponRepo.findByCodigoAndEstadoNot(nombreCodigo, EstadoCupon.ELIMINADO);
        Cupon cuponEncontrado = null;
        if(cuponNuevo.isPresent()) {
            cuponEncontrado = cuponNuevo.get();
        }
        assert cuponEncontrado != null;
        String id = cuponEncontrado.getId();

        cuponRepo.deleteById(id);
        Optional<Cupon> cuponOptional = cuponRepo.findById(id);
        Assertions.assertTrue(cuponOptional.isEmpty());
    }

    @Test
    public void actualizarCuponTest() {

        String nombreCodigo = "CUPONACTUALIZAR";

        Cupon cupon = Cupon.builder()
                .codigo(nombreCodigo)
                .nombre("Descuento del 1.1.%")
                .porcentajeDescuento(1.1)
                .estadoCupon(EstadoCupon.ACTIVO)
                .tipoCupon(TipoCupon.GENERAL)
                .fechaVencimiento(LocalDate.now().plusDays(10))
                .build();

        cuponRepo.save(cupon);
        Optional<Cupon> cuponNuevo = cuponRepo.findByCodigoAndEstadoNot(nombreCodigo, EstadoCupon.ELIMINADO);
        Cupon cuponEncontrado = null;
        if(cuponNuevo.isPresent()) {
            cuponEncontrado = cuponNuevo.get();
            cuponEncontrado.setCodigo("NUEVONOMBRE");
            cuponRepo.save(cuponEncontrado);
        }
        Optional<Cupon> cuponModificado = cuponRepo.findByCodigoAndEstadoNot("NUEVONOMBRE", EstadoCupon.ELIMINADO);
        cuponModificado.ifPresent(value -> Assertions.assertEquals("NUEVONOMBRE", value.getCodigo()));

    }

    @Test
    public void obtenerCuponTest() {

        String nombreCodigo = "CUPONOBTENERPORID";

        Cupon cupon = Cupon.builder()
                .codigo(nombreCodigo)
                .nombre("Descuento del 1.1.%")
                .porcentajeDescuento(1.1)
                .estadoCupon(EstadoCupon.ACTIVO)
                .tipoCupon(TipoCupon.GENERAL)
                .fechaVencimiento(LocalDate.now().plusDays(10))
                .build();

        cuponRepo.save(cupon);
        Optional<Cupon> cuponNuevo = cuponRepo.findByCodigoAndEstadoNot(nombreCodigo, EstadoCupon.ELIMINADO);
        Cupon cuponEncontrado = null;
        if(cuponNuevo.isPresent()) {
            cuponEncontrado = cuponNuevo.get();
        }
        assert cuponEncontrado != null;

        String id = cuponEncontrado.getId();
        Optional<Cupon> cuponOptional = cuponRepo.findByIdAndEstadoNot(id, EstadoCupon.ELIMINADO);

        if (cuponOptional.isPresent()) {
            Cupon cuponObtenido = cuponOptional.get();
            Assertions.assertNotNull(cuponObtenido);
        }
    }

}