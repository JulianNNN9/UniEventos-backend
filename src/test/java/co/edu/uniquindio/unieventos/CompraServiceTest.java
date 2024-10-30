package co.edu.uniquindio.unieventos;

import co.edu.uniquindio.unieventos.dto.compra.CrearCompraDTO;
import co.edu.uniquindio.unieventos.dto.compra.InformacionItemCompraDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.CrearUsuarioDTO;
import co.edu.uniquindio.unieventos.dto.cupon.CrearCuponDTO;
import co.edu.uniquindio.unieventos.exceptions.CuponUsadoException;
import co.edu.uniquindio.unieventos.exceptions.EntradasInsuficientesException;
import co.edu.uniquindio.unieventos.exceptions.RecursoEncontradoException;
import co.edu.uniquindio.unieventos.exceptions.RecursoNoEncontradoException;
import co.edu.uniquindio.unieventos.model.*;
import co.edu.uniquindio.unieventos.repositories.UsuarioRepo;
import co.edu.uniquindio.unieventos.services.implementacion.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
/*
IMPORTANTE: Se necesita cargar el Script de datase.js en la base de datos para poder realizar correctamente las
            Pruebas
 */

@SpringBootTest
public class CompraServiceTest {
    @Autowired
    private UsuarioServiceImple usuarioServiceImple;
    @Autowired
    private CompraServiceImple compraServiceImple;
    @Autowired
    private CuponServiceImple cuponServiceImple;
    @Autowired
    private EventoServiceImple eventoServiceImple;
    @Test
    public void testObtenerCompraPorId() {
        //ID Compra 1 DEL DATASET
        String idCompra = "66fc30f01f299be000110b36";
        try {
            assertDoesNotThrow(() -> {
                Compra resultado = compraServiceImple.obtenerCompra(idCompra);
                assertNotNull(resultado);
            });
        } catch (Exception e) {
            fail("Validacion de obtenerCuponPorIdTest Falló" + e.getMessage());
        }
    }
    @Test
    public void testObtenerCompraPorIdNoExisteError() {
        //ID no EXISTENTE en la coleccion de compras
        String idCompra = "1234";
        try {
            compraServiceImple.obtenerCompra(idCompra);
            fail("Validacion de testObtenerCompraPorIdNoExisteError Falló ");
        } catch (RecursoNoEncontradoException e) {
            assertEquals("Compra no encontrada", e.getMessage());
        } catch (Exception e){
            fail("Validacion de testObtenerCompraPorIdNoExisteError Falló " + e.getMessage());

        }
    }
    @Test
    public void testObtenerComprasPorIdUsuario() {
        //ID Usuario 3 DEL DATASET
        String idUsuario = "66b2c1517f3b340441ffdeb1";
        try {
            //El usuario 3 tiene 1 compra
            List<Compra>comprasUsuarioList = compraServiceImple.obtenerComprasUsuario(idUsuario);
            assertEquals(1, comprasUsuarioList.size());
        } catch (Exception e) {
            fail("Validacion de obtenerCuponPorIdTest Falló" + e.getMessage());
        }
    }
    @Test
    public void testObtenerComprasPorIdUsuarioNoExisteError() {
        //ID no EXISTENTE en la coleccion de compras
        String idUsuario = "1234";
        try {
            //Como el Usuario no existe, retorna una lista vacia de compras
            List<Compra>comprasUsuarioList = compraServiceImple.obtenerComprasUsuario(idUsuario);
            assertEquals(0, comprasUsuarioList.size());
        } catch (Exception e){
            fail("Validacion de testObtenerComprasPorIdUsuarioNoExisteError Falló " + e.getMessage());

        }
    }

    @Test
    void testCrearCompraConCupon() throws Exception {
        // Codigo DEL CUPON 2 del dataset donde su estado es ACTIVO
        String codigoCupon = "PRIMERACOMPRA";
        //Creamos un Usuario nuevo para aplicar a 'PRIMERACOMPRA'
        CrearUsuarioDTO registroClienteDTO = new CrearUsuarioDTO(
                "222222255",
                "Luis Carlos",
                "Barrio Los Naranjo Calle 11",
                "344554444",
                "luiscarlos@email.com",
                "micontrasenia"
        );
        String idUsuario = usuarioServiceImple.crearUsuario(registroClienteDTO);

        CrearCompraDTO crearCompraDTO = new CrearCompraDTO(
                idUsuario,
                List.of(new InformacionItemCompraDTO("66b2c1517f3b340441ffdebc", "PLATINO", 2, 180000.0)),
                codigoCupon
        );
        Assertions.assertDoesNotThrow(() -> compraServiceImple.crearCompra(crearCompraDTO));

    }
    @Test
    void testCrearCompraSinCupon() throws Exception {

        //ID USUARIO 2 del Dataset
        String idUsuario = "66b2b14dd9219911cd34f2c1";

        CrearCompraDTO crearCompraDTO = new CrearCompraDTO(
                idUsuario,
                List.of(new InformacionItemCompraDTO("66b2c1517f3b340441ffdebc", "PLATINO", 2, 180000.0)),
                null
        );
        Assertions.assertDoesNotThrow(() -> compraServiceImple.crearCompra(crearCompraDTO));

    }
    @Test
    void testCrearCompra_CuponInactivoException() {
        // Codigo DEL CUPON 3 del dataset donde su estado es INACTIVO
        String codigoCupon = "BANCOLOMBIA";
        //Simulamos la compra con el Usuario 3 del Dataset
        String idUsuario = "66b2c1517f3b340441ffdeb1";

        CrearCompraDTO crearCompraDTO = new CrearCompraDTO(
                idUsuario,
                List.of(new InformacionItemCompraDTO("66b2c1517f3b340441ffdebc", "PLATINO", 4, 180000.0)),
                codigoCupon
        );
        try{
            compraServiceImple.crearCompra(crearCompraDTO);
            fail("Validacion de testCrearCompra_CuponInactivoException Falló ");
        } catch (IllegalArgumentException e){
            assertEquals("El cupón no está disponible o ha expirado", e.getMessage());
        } catch (Exception e){
            fail("Validacion de testCrearCompra_CuponInactivoException Falló " + e.getMessage());
        }

    }
    @Test
    void testCrearCompra_CuponExpiradoException() throws Exception {
        String codigoCupon = "CUPONVENCIDO";
        //Creamos un cupon y le cambios la fecha de vencimiento por ayer para expirarlo
        CrearCuponDTO crearCuponDTO = new CrearCuponDTO(
                codigoCupon,
                "Este cupun esta vencido, una lastima",
                1.0,
                EstadoCupon.ACTIVO, //Aunque el estado esté activo, va a pasar a inactivo luego de expirarse  en la excepcion
                TipoCupon.GENERAL,
                LocalDate.now().minusDays(1), //Expiramos el cupon
                null
        );
        cuponServiceImple.crearCupon(crearCuponDTO);

        //Simulamos la compra con el Usuario 3 del Dataset
        String idUsuario = "66b2c1517f3b340441ffdeb1";

        CrearCompraDTO crearCompraDTO = new CrearCompraDTO(
                idUsuario,
                List.of(new InformacionItemCompraDTO("66b2c1517f3b340441ffdebc", "PLATINO", 4, 180000.0)),
                codigoCupon
        );
        try{
            compraServiceImple.crearCompra(crearCompraDTO);
            fail("Validacion de testCrearCompra_CuponExpiradoException Falló ");
        } catch (IllegalArgumentException e){
            assertEquals("El cupon ha expirado", e.getMessage());
        } catch (Exception e){
            fail("Validacion de testCrearCompra_CuponExpiradoException Falló " + e.getMessage());
        }

    }

    @Test
    void testCrearCompra_CuponGeneralRedimidoException() {
        /*
        El Usuario 2 en el dataset, ya tiene una compra con el cupon 'PRIMERACOMPRA'
        OJO la compra tiene que tener el EstadoCompra en COMPLETADA para verificar que se realizo la compra
         */
        String codigoCupon = "PRIMERACOMPRA";
        //Simulamos la compra con el Usuario 2 del Dataset
        String idUsuario = "66b2b14dd9219911cd34f2c1";

        CrearCompraDTO crearCompraDTO = new CrearCompraDTO(
                idUsuario,
                List.of(new InformacionItemCompraDTO("66b2c1517f3b340441ffdebc", "PLATINO", 4, 180000.0)),
                codigoCupon
        );
        try{
            compraServiceImple.crearCompra(crearCompraDTO);
            fail("Validacion de testCrearCompra_CuponGeneralRedimidoException Falló ");
        } catch (RecursoEncontradoException e){
            assertEquals("Este cupón ya ha sido redimido por el usuario", e.getMessage());
        } catch (Exception e){
            fail("Validacion de testCrearCompra_CuponGeneralRedimidoException Falló " + e.getMessage());
        }

    }
    @Test
    void testCrearCompra_CuponLocalidadNoEncontradaException() {
        /*
        El Usuario 2 en el dataset, ya tiene una compra con el cupon 'PRIMERACOMPRA'
        OJO la compra tiene que tener el EstadoCompra en COMPLETADA para verificar que se realizo la compra
         */
        //Simulamos la compra con el Usuario 2 del Dataset
        String idUsuario = "66b2b14dd9219911cd34f2c1";
        String idEvento = "66b2c1517f3b340441ffdebc"; //ID Evento 4 del dataset (No tiene localidad 'ORO')
        String localidadNoExiste = "ORO";

        CrearCompraDTO crearCompraDTO = new CrearCompraDTO(
                idUsuario,
                List.of(new InformacionItemCompraDTO(idEvento, localidadNoExiste, 2, 40000.0)),
                null
        );
        try{
            compraServiceImple.crearCompra(crearCompraDTO);
            fail("Validacion de testCrearCompra_CuponLocalidadNoEncontradaException Falló ");
        } catch (RecursoNoEncontradoException e){
            assertEquals("Localidad no encontrada", e.getMessage());
        } catch (Exception e){
            fail("Validacion de testCrearCompra_CuponLocalidadNoEncontradaException Falló " + e.getMessage());
        }

    }
    @Test
    void testCrearCompra_CuponCantidadExcedidaException() {

        //Simulamos la compra con el Usuario 2 del Dataset
        String idUsuario = "66b2b14dd9219911cd34f2c1";
        String idEvento = "66b2c1517f3b340441ffdebc"; //ID Evento 4 del dataset (No tiene localidad 'ORO')
        String localidadCorrecta = "PLATINO";
        int cantidadExcedida = 100; //La localidad 'PLATINO' del evento 4 del dataset no tiene 100 entradasDisponibles

        CrearCompraDTO crearCompraDTO = new CrearCompraDTO(
                idUsuario,
                List.of(new InformacionItemCompraDTO(idEvento, localidadCorrecta, cantidadExcedida, 180000.0)),
                null
        );
        try{
            compraServiceImple.crearCompra(crearCompraDTO);
            fail("Validacion de testCrearCompra_CuponCantidadExcedidaException Falló ");
        } catch (EntradasInsuficientesException e){
            assertEquals("No hay suficientes entradas restantes para la localidad", e.getMessage());
        } catch (Exception e){
            fail("Validacion de testCrearCompra_CuponCantidadExcedidaException Falló " + e.getMessage());
        }

    }
    @Test
    void testCancelarCompra() throws Exception {
        //ID DE LA COMPRA 1 del Dataset con Estado PENDIENTE
        String idCompra = "66fc30f01f299be000110b36";
        // Ejecutar el método cancelarCompra
        String resultado = compraServiceImple.cancelarCompra(idCompra);
        // Verificar que la compra fue cancelada

        assertEquals("Compra cancelada exitosamente", resultado);

        Compra compraCancelada = compraServiceImple.obtenerCompra(idCompra);
        assertEquals(EstadoCompra.CANCELADA, compraCancelada.getEstadoCompra());

        //Evento 1 del primer item de la compra
        String idEventoLocalidad1 = compraCancelada.getItemsCompra().get(0).getEvento().getId();
        Evento evento = eventoServiceImple.obtenerEvento(idEventoLocalidad1);
        //Antes de Cancelarla habia 98 entradas en la localidad 'VIP' del primer evento del item de la compra
        // al cancelarlo deberian quedar 100
        assertEquals(100, evento.getLocalidades().get(0).getEntradasRestantes());

    }
    @Test
    void testCancelarCompraNoPendienteException() {
        String idCompra = "66fc31634ed5f7a3186e0e19"; //Compra con Estado COMPLETADA
        try{
            compraServiceImple.cancelarCompra(idCompra);
            fail("Validacion de testCancelarCompraNoPendienteException Falló ");
        } catch (IllegalArgumentException e){
            assertEquals("La compra debe estar PENDIENTE", e.getMessage());
        } catch (Exception e){
            fail("Validacion de testCancelarCompraNoPendienteException Falló " + e.getMessage());
        }

    }
    @Test
    void testCancelarCompraIdNoExisteException() {
        // ID de compra inexistente
        String idCompra = "1223344";
        try{
            compraServiceImple.cancelarCompra(idCompra);
            fail("Validacion de testCancelarCompraIdNoExisteException Falló ");
        } catch (RecursoNoEncontradoException e){
            assertEquals("Compra no encontrada", e.getMessage());
        } catch (Exception e){
            fail("Validacion de testCancelarCompraIdNoExisteException Falló " + e.getMessage());
        }

    }
}
