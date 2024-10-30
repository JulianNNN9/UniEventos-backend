package co.edu.uniquindio.unieventos;

import co.edu.uniquindio.unieventos.dto.cupon.CrearCuponDTO;
import co.edu.uniquindio.unieventos.dto.cupon.EditarCuponDTO;
import co.edu.uniquindio.unieventos.exceptions.RecursoEncontradoException;
import co.edu.uniquindio.unieventos.exceptions.RecursoNoEncontradoException;
import co.edu.uniquindio.unieventos.model.Cupon;
import co.edu.uniquindio.unieventos.model.EstadoCupon;
import co.edu.uniquindio.unieventos.model.TipoCupon;
import co.edu.uniquindio.unieventos.repositories.CuponRepo;
import co.edu.uniquindio.unieventos.services.implementacion.CuponServiceImple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
/*
IMPORTANTE: Se necesita cargar el Script de datase.js en la base de datos para poder realizar correctamente las
            Pruebas
 */
@SpringBootTest
public class CuponServiceTest {

    @Autowired
    private CuponServiceImple cuponServiceImple;

    @Autowired
    private CuponRepo cuponRepo;


    @Test
    public void obtenerCuponPorIdTest() {
        //ID CUPON 2 DEL DATASET
        String idCupon = "66b2c1517f3b340441ffdeb5";
        try {

            assertDoesNotThrow(() -> {
                Cupon resultado = cuponServiceImple.obtenerCuponPorId(idCupon);
                assertNotNull(resultado);
            });
        } catch (Exception e) {
            fail("Validacion de obtenerCuponPorIdTest Falló" + e.getMessage());
        }
    }
    @Test
    public void obtenerCuponPorIdNoExisteErrorTest() {
        //ID no EXISTENTE en la coleccion de cupones
        String idCupon = "1234";
        try {
            cuponServiceImple.obtenerCuponPorId(idCupon);
            fail("Validacion de obtenerCuponPorIdNoExisteErrorTest Falló ");
        } catch (RecursoNoEncontradoException e) {
            assertEquals("Cupón no encontrado", e.getMessage());
        } catch (Exception e){
            fail("Validacion de obtenerCuponPorIdNoExisteErrorTest Falló " + e.getMessage());

        }
    }

    @Test
    public void obtenerCuponPorCodigoTest() {
        //CODIGO DEL CUPON 2 DEL DATASET
        String codigoCupon = "PRIMERACOMPRA";
        try {

            assertDoesNotThrow(() -> {
                Cupon resultado = cuponServiceImple.obtenerCuponPorCodigo(codigoCupon);
                assertNotNull(resultado);
            });
        } catch (Exception e) {
            fail("Validacion de obtenerCuponPorCodigoTest Falló " + e.getMessage());
        }
    }
    @Test
    public void obtenerCuponPorCodigoNoExisteErrorTest() {
        //ID no EXISTENTE en la coleccion de cupones
        String codigoNoExiste = "HOLA";
        try {
            cuponServiceImple.obtenerCuponPorCodigo(codigoNoExiste);
            fail("Validacion de obtenerCuponPorCodigoNoExisteErrorTest Falló ");
        } catch (RecursoNoEncontradoException e) {
            assertEquals("Cupón no encontrado", e.getMessage());
        } catch (Exception e){
            fail("Validacion de obtenerCuponPorCodigoNoExisteErrorTest Falló " + e.getMessage());

        }
    }
    @Test
    public void obtenerCuponPorCodigoYIdUsuarioTest() {
        //CODIGO DEL CUPON 2 DEL DATASET
        String codigoCupon = "5URVPL";
        String idUsuario = "6722b8784c1f6c5a95e01de0";
        try {
            List<Cupon> cupon = cuponServiceImple.obtenerListaCuponPorIdUsuario(idUsuario);
            System.out.println(cupon.size());
            assertDoesNotThrow(() -> {
                Cupon resultado = cuponServiceImple.obtenerCuponPorCodigoYIdUsuario(codigoCupon, idUsuario);
                assertNotNull(resultado);
            });
        } catch (Exception e) {
            fail("Validacion de obtenerCuponPorCodigoTest Falló " + e.getMessage());
        }
    }
    @Test
    public void crearCuponTest() {
        CrearCuponDTO crearCuponDTO = new CrearCuponDTO(
                "AMORYAMISTAD",
                "Descuento del 15.5% por el dia del amor y amistad",
                15.5,
                EstadoCupon.ACTIVO,
                TipoCupon.GENERAL,
                LocalDate.now().plusDays(30),
                null
        );
        assertDoesNotThrow(() -> {
            String resultado = cuponServiceImple.crearCupon(crearCuponDTO);
            assertEquals("Cupon creado exitosamente", resultado);
        });
    }

    @Test
    public void crearCuponYaExistenteErrorTest() {
        //CUPON 2 DEL DATASET
        String nombreCuponExiste = "PRIMERACOMPRA";
        CrearCuponDTO crearCuponDTO = new CrearCuponDTO(
                nombreCuponExiste,
                "Descuento del 10% para la primera compra",
                10.0,
                EstadoCupon.ACTIVO,
                TipoCupon.GENERAL,
                LocalDate.now().plusDays(30),
                null
        );
        try {
            cuponServiceImple.crearCupon(crearCuponDTO);
            fail("Validacion de crearCuponYaExistenteErrorTest Falló ");
        } catch (RecursoEncontradoException e) {
            assertEquals("El cupon ya existe", e.getMessage());
        } catch (Exception e){
            fail("Validacion de crearCuponYaExistenteErrorTest Falló " + e.getMessage());

        }
    }

    @Test
    public void editarCuponTest() {
        //ID DEL CUPON 1 DEL DATASET
        String idCupon = "66b2c1517f3b340441ffdeb4";

        //Creamos el DTO para editar el cupon
        EditarCuponDTO editarCuponDTO = new EditarCuponDTO(
                idCupon,
                "CUPON55",
                "CUPON MODIFICADO",
                6.0,
                EstadoCupon.ACTIVO,
                TipoCupon.UNICO,
                LocalDate.now().plusDays(30)
        );
        try{
            cuponServiceImple.editarCupon(editarCuponDTO);
            Optional<Cupon> cuponEncontrado = cuponRepo.findByIdAndEstadoNot(idCupon, EstadoCupon.ELIMINADO);
            Cupon cupon = null;
            if(cuponEncontrado.isPresent()){
                cupon = cuponEncontrado.get();
            }
            //Comparamos si el nombre cambio en el cupon
            assertEquals("CUPON MODIFICADO", cupon.getNombre());
        } catch (Exception e){
            fail("Validacion de editarCuponTest Falló " + e.getMessage());
        }
    }

    @Test
    public void editarCuponNoExistenteErrorTest() {
        String idCupon = "1234"; //ID Erroneo que no existe en la base de datos

        EditarCuponDTO editarCuponDTO = new EditarCuponDTO(
                idCupon, //ID ERRONEO
                "CUPON55",
                "CUPON MODIFICADO",
                6.0,
                EstadoCupon.ACTIVO,
                TipoCupon.UNICO,
                LocalDate.now().plusDays(30)
        );
        try{
            cuponServiceImple.editarCupon(editarCuponDTO);
            fail("Validacion de editarCuponNoExistenteErrorTest Falló ");
        } catch (RecursoNoEncontradoException e){
            assertEquals("Cupón no encontrado", e.getMessage());
        } catch (Exception e){
            fail("Validacion de editarCuponNoExistenteErrorTest Falló " + e.getMessage());
        }
    }

    @Test
    public void eliminarCuponTest() throws RecursoNoEncontradoException {
        //ID CUPON 4 DATASET
        String idCupon = "66b2c1517f3b340441ffdeb7";
        //Se elimina el cupon
        cuponServiceImple.eliminarCupon(idCupon);
        //Al intentar encontrar el cupon arroja una excepcion porque no existe
        assertThrows(RecursoNoEncontradoException.class, () -> cuponServiceImple.obtenerCuponPorId(idCupon) );

    }

    @Test
    public void eliminarCuponNoExistenteTest(){
        String idCupon = "1234"; //ID ERRONEO DEL CUPON
        //Se intenta eliminar el cupon
        try{
            cuponServiceImple.eliminarCupon(idCupon);
            fail("Validacion de eliminarCuponNoExistenteTest Falló ");
        } catch (RecursoNoEncontradoException e){
            assertEquals("Cupón no encontrado", e.getMessage());
        } catch (Exception e){
            fail("Validacion de eliminarCuponNoExistenteTest Falló " + e.getMessage());

        }

    }


}
