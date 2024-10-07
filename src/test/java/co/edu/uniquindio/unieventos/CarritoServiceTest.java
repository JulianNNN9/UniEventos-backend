package co.edu.uniquindio.unieventos;

import co.edu.uniquindio.unieventos.dto.carrito.AgregarItemDTO;
import co.edu.uniquindio.unieventos.dto.carrito.EditarCarritoDTO;
import co.edu.uniquindio.unieventos.dto.carrito.EliminarDelCarritoDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.CrearUsuarioDTO;
import co.edu.uniquindio.unieventos.exceptions.RecursoNoEncontradoException;
import co.edu.uniquindio.unieventos.model.Carrito;
import co.edu.uniquindio.unieventos.model.DetalleCarrito;
import co.edu.uniquindio.unieventos.repositories.CarritoRepo;
import co.edu.uniquindio.unieventos.services.implementacion.CarritoServiceImple;
import co.edu.uniquindio.unieventos.services.implementacion.UsuarioServiceImple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CarritoServiceTest {

    @Autowired
    private CarritoServiceImple carritoServiceImple;

    @Autowired
    private UsuarioServiceImple usuarioServiceImple;
    @Autowired
    private CarritoRepo carritoRepo;

    @Test
    public void obtenerCarritoPorIdUsuarioTest() {
        //id del Usuario del Carrito 4 del Dataset
        String idUsuario = "66b2c1517f3b340441ffdeb2";
        try {
            assertDoesNotThrow(() -> {
                Carrito resultado = carritoServiceImple.obtenerCarritoPorIdUsuario(idUsuario);
                assertNotNull(resultado);
            });
        } catch (Exception e) {
            fail("Validacion de obtenerCarritoPorIdTest Falló " + e.getMessage());
        }
    }
    @Test
    public void obtenerCarritoPorIdUsuarioNoExisteErrorTest() {
        //ID no EXISTENTE en la coleccion de carritos
        String idCarrito = "1234";
        try {
            carritoServiceImple.obtenerCarritoPorIdUsuario(idCarrito);
            fail("Validacion de obtenerCarritoPorIdUsuarioNoExisteErrorTest Falló ");
        } catch (RecursoNoEncontradoException e) {
            assertEquals("Carrito no encontrado", e.getMessage());
        } catch (Exception e){
            fail("Validacion de obtenerCarritoPorIdUsuarioNoExisteErrorTest Falló " + e.getMessage());

        }
    }
    @Test
    public void obtenerCarritoPorIdTest() {
        //ID Carrito 1 DEL DATASET
        String idCarrito = "64db45abcf1b8a0001a0b8a1";
        try {
            assertDoesNotThrow(() -> {
                Carrito resultado = carritoServiceImple.obtenerCarrito(idCarrito);
                assertNotNull(resultado);
            });
        } catch (Exception e) {
            fail("Validacion de obtenerCarritoPorIdTest Falló " + e.getMessage());
        }
    }
    @Test
    public void obtenerCarritoPorIdNoExisteErrorTest() {
        //ID no EXISTENTE en la coleccion de carritos
        String idCarrito = "1234";
        try {
            carritoServiceImple.obtenerCarrito(idCarrito);
            fail("Validacion de obtenerCarritoPorIdNoExisteErrorTest Falló ");
        } catch (RecursoNoEncontradoException e) {
            assertEquals("Carrito no encontrado", e.getMessage());
        } catch (Exception e){
            fail("Validacion de obtenerCarritoPorIdNoExisteErrorTest Falló " + e.getMessage());

        }
    }
    @Test
    public void crearCarritoTest() throws Exception {
        //Creamos un nuevo usuario para crearle un carrito
        CrearUsuarioDTO registroClienteDTO = new CrearUsuarioDTO(
                "999999",
                "Mario Castañeda",
                "Calle 2 # 3-4",
                "315678921",
                "marioc@email.com",
                "micontrasenia"
        );
        String id = usuarioServiceImple.crearUsuario(registroClienteDTO);

        assertDoesNotThrow(() -> {
            String resultado = carritoServiceImple.crearCarrito(id);
            assertEquals("Carrito creado exitosamente", resultado);
        });
    }

    @Test
    public void agregarAlCarritoTest() {
        String idCarrito = "64db45abcf1b8a0001a0b8a1"; //ID CARRITO 1 del Dataset
        String idEvento = "66b2c1517f3b340441ffdebb"; //ID EVENTO 3 del Dataset
        AgregarItemDTO agregarItemDTO = new AgregarItemDTO(
                idCarrito, // ID del carrito
                new DetalleCarrito(
                        2,
                        "VIP",
                        idEvento)
        );

        assertDoesNotThrow(() -> {
            String resultado = carritoServiceImple.agregarAlCarrito(agregarItemDTO);
            assertEquals("Item agregado al carrito con éxito", resultado);
        });
    }

    @Test
    public void agregarAlCarritoNoExisteIdErrorTest() {
        String idCarritoInvalido = "12324"; //ID ERRORENO DEL CARRITO
        AgregarItemDTO agregarItemDTO = new AgregarItemDTO(
                idCarritoInvalido, // ID del carrito Erroneo
                new DetalleCarrito(1, "VIP", "12345") // Evento no existente
        );
        try {
            carritoServiceImple.agregarAlCarrito(agregarItemDTO);
            fail("Validacion de agregarAlCarritoNoExisteIdErrorTest Falló");
        } catch (RecursoNoEncontradoException e) {
            assertEquals("Carrito no encontrado", e.getMessage());
        } catch (Exception e) {
            fail("Validacion de agregarAlCarritoNoExisteIdErrorTest Falló " + e.getMessage());
        }
    }
    @Test
    public void agregarAlCarritoEventoNoExisteErrorTest() {
        String idCarrito = "64db45abcf1b8a0001a0b8a1"; //ID CARRITO 1 del Dataset
        String idEvento = "12345"; //ID ERRONEO DEL EVENTO
        AgregarItemDTO agregarItemDTO = new AgregarItemDTO(
                idCarrito, // ID del carrito
                new DetalleCarrito(1, "GENERAL", idEvento) // Evento no existente
        );
        try {
            carritoServiceImple.agregarAlCarrito(agregarItemDTO);
            fail("Validacion de agregarAlCarritoEventoNoExisteErrorTest Falló");
        } catch (RecursoNoEncontradoException e) {
            assertEquals("Evento no encontrado", e.getMessage());
        } catch (Exception e) {
            fail("Validacion de agregarAlCarritoEventoNoExisteErrorTest Falló " + e.getMessage());
        }
    }
    @Test
    public void agregarAlCarritoLocalidadNoExisteErrorTest() {
        String idCarrito = "64db45abcf1b8a0001a0b8a2"; //ID CARRITO 2 del Dataset
        String idEvento = "66b2c1517f3b340441ffdebc"; // ID válido del evento 4 del dataset
        String nombreLocalidadNoExiste = "PREMIUM"; // Localidad que no existe en el evento

        AgregarItemDTO agregarItemDTO = new AgregarItemDTO(
                idCarrito,
                new DetalleCarrito(1, nombreLocalidadNoExiste, idEvento)
        );

        try {
            carritoServiceImple.agregarAlCarrito(agregarItemDTO);
            fail("Validacion de agregarAlCarritoLocalidadNoExisteErrorTest Falló");
        } catch (RecursoNoEncontradoException e) {
            assertEquals("Localidad no encontrada", e.getMessage());
        } catch (Exception e) {
            fail("Validacion de agregarAlCarritoLocalidadNoExisteErrorTest Falló " + e.getMessage());
        }
    }
    @Test
    public void agregarAlCarritoCantidadMayorEntradasDisponiblesErrorTest() {
        String idCarrito = "64db45abcf1b8a0001a0b8a1"; // ID válido del carrito 1 del dataset
        String idEvento = "66b2c1517f3b340441ffdebc"; // ID válido del evento 4
        String nombreLocalidad = "PLATINO"; // Localidad válida
        int cantidadExcedida = 100; // Cantidad que excede las entradas  //HAY 99 restantes

        AgregarItemDTO agregarItemDTO = new AgregarItemDTO(
                idCarrito,
                new DetalleCarrito(cantidadExcedida, nombreLocalidad, idEvento)
        );

        try {
            carritoServiceImple.agregarAlCarrito(agregarItemDTO);
            fail("Validacion de agregarAlCarritoCantidadMayorEntradasDisponiblesErrorTest Falló");
        } catch (IllegalArgumentException e) {
            assertEquals("No hay suficientes entradas disponibles en la localidad seleccionada", e.getMessage());
        } catch (Exception e) {
            fail("Validacion de agregarAlCarritoCantidadMayorEntradasDisponiblesErrorTest Falló " + e.getMessage());
        }
    }
    @Test
    public void eliminarDelCarritoTest() throws Exception {
        //Este carrito solo tiene un Item en
        String idCarrito = "64db45abcf1b8a0001a0b8a5"; //ID del carrito 5 del Dataset
        String idEvento = "66b2c1517f3b340441ffdebd"; //ID del evento del primer item del carrito

        EliminarDelCarritoDTO eliminarDelCarritoDTO = new EliminarDelCarritoDTO(
                idCarrito, // ID del carrito
                "EXPOSITOR",
                idEvento // ID del evento
        );

        carritoServiceImple.eliminarDelCarrito(eliminarDelCarritoDTO);
        Carrito carrito = carritoServiceImple.obtenerCarrito(idCarrito);
        assertEquals(0, carrito.getItemsCarrito().size());
    }
    @Test
    public void eliminarDelCarritoNoExisteTest() {
        String idCarrito = "1234"; //ID ERRONEO de un carrito
        String idEvento = "1234";

        EliminarDelCarritoDTO eliminarDelCarritoDTO = new EliminarDelCarritoDTO(
                idCarrito, // ID del carrito Erroneo
                "EXPOSITOR", //Localidad
                idEvento// ID del evento
        );
        try{
            carritoServiceImple.eliminarDelCarrito(eliminarDelCarritoDTO);
            fail("Validacion de eliminarDelCarritoNoExisteTest Falló ");
        } catch (RecursoNoEncontradoException e){
            assertEquals("Carrito no encontrado", e.getMessage());
        } catch (Exception e){
            fail("Validacion de eliminarDelCarritoNoExisteTest Falló " + e.getMessage());
        }
    }
    @Test
    public void editarCarritoTest() {
        String idCarrito = "64db45abcf1b8a0001a0b8a3"; //ID del carrito 3 del Dataset
        String idEvento = "66b2c1517f3b340441ffdebc"; //ID del evento del primer item del carrito

        EditarCarritoDTO editarCarritoDTO = new EditarCarritoDTO(
                idCarrito, // ID del carrito
                "PLATINO", // Localidad
                idEvento, // ID del evento
                4 // Nueva cantidad
        );

        assertDoesNotThrow(() -> {
            String resultado = carritoServiceImple.editarCarrito(editarCarritoDTO);
            assertEquals("Carrito editado exitosamente", resultado);
        });
    }

    @Test
    public void editarCarritoNoExisteErrorTest() {
        String idCarrito = "1234"; //ID ERRONEO de un carrito
        String idEvento = "1234";

        EditarCarritoDTO editarCarritoDTO = new EditarCarritoDTO(
                idCarrito, // ID del carrito No existente
                "VIP", // Localidad
                idEvento, // Id del evento
                1 // Nueva cantidad
        );

        try {
            carritoServiceImple.editarCarrito(editarCarritoDTO);
            fail("Validacion de editarCarritoNoExisteErrorTest Falló");
        } catch (RecursoNoEncontradoException e) {
            assertEquals("Carrito no encontrado", e.getMessage());
        } catch (Exception e) {
            fail("Validacion de editarCarritoNoExisteErrorTest Falló " + e.getMessage());
        }
    }
    @Test
    public void editarCarritoEventoNoExisteErrorTest() {
        String idCarrito = "64db45abcf1b8a0001a0b8a1"; // ID válido del carrito 1 DEL DATASET
        String idEventoInvalido = "1234"; // ID del evento no existente

        EditarCarritoDTO editarCarritoDTO = new EditarCarritoDTO(
                idCarrito,
                "VIP", // Localidad
                idEventoInvalido, // Evento no existente
                1 // Nueva cantidad
        );

        try {
            carritoServiceImple.editarCarrito(editarCarritoDTO);
            fail("Validacion de editarCarritoEventoNoExisteErrorTest Falló");
        } catch (RecursoNoEncontradoException e) {
            assertEquals("Evento no encontrado", e.getMessage());
        } catch (Exception e) {
            fail("Validacion de editarCarritoEventoNoExisteErrorTest Falló " + e.getMessage());
        }
    }
    @Test
    public void editarCarritoItemNoExisteErrorTest() {
        String idCarrito = "64db45abcf1b8a0001a0b8a1"; // ID válido del carrito 1
        String idEvento = "66b2c1517f3b340441ffdeb9"; // ID válido del evento del carrito
        String nombreLocalidadInvalido = "PREMIUM"; // Localidad no existente en el carrito

        EditarCarritoDTO editarCarritoDTO = new EditarCarritoDTO(
                idCarrito,
                nombreLocalidadInvalido, // Localidad no existente en el carrito
                idEvento,
                1 // Nueva cantidad
        );

        try {
            carritoServiceImple.editarCarrito(editarCarritoDTO);
            fail("Validacion de editarCarritoItemNoExisteErrorTest Falló");
        } catch (RecursoNoEncontradoException e) {
            assertEquals("Item no encontrado en el carrito", e.getMessage());
        } catch (Exception e) {
            fail("Validacion de editarCarritoItemNoExisteErrorTest Falló " + e.getMessage());
        }
    }
    @Test
    public void editarCarritoLocalidadNoExisteErrorTest() throws RecursoNoEncontradoException {
        //Este error se ejecuta cuando un carrito tiene un item de un evento (DetalleCarrito)
        //que no existe en el evento, asi que vamos a simularlo
        String idCarrito = "64db45abcf1b8a0001a0b8a1"; // ID válido del carrito
        String idEvento = "66b2c1517f3b340441ffdeb9"; // ID válido del evento
        String nombreLocalidadInvalido = "EXCLUSIVA"; // Localidad que no existe en el evento

        DetalleCarrito detalleCarrito = new DetalleCarrito(
                5,
                "EXCLUSIVA", //LOCALIDAD QUE NO EXISTE
                idEvento); //ID EVENTO 1 DEL DATASET
        //Guardamos el nuevo item al carrito, en el que la localidad no existe en el evento
        Carrito carrito = carritoServiceImple.obtenerCarrito(idCarrito); //Obtenemos el Carrito 1 del dataset
        carrito.getItemsCarrito().add(detalleCarrito);
        carritoRepo.save(carrito);

        EditarCarritoDTO editarCarritoDTO = new EditarCarritoDTO(
                idCarrito,
                nombreLocalidadInvalido, // Localidad no existente
                idEvento,
                1 // Nueva cantidad
        );

        try {
            carritoServiceImple.editarCarrito(editarCarritoDTO);
            fail("Validacion de editarCarritoLocalidadNoExisteErrorTest Falló");
        } catch (RecursoNoEncontradoException e) {
            assertEquals("Localidad no encontrada", e.getMessage());
        } catch (Exception e) {
            fail("Validacion de editarCarritoLocalidadNoExisteErrorTest Falló " + e.getMessage());
        }
    }
    @Test
    public void editarCarritoCantidadMayorEntradasDisponiblesErrorTest() {
        String idCarrito = "64db45abcf1b8a0001a0b8a1"; // ID válido del carrito 1 del dataset
        String idEvento = "66b2c1517f3b340441ffdeb9"; // ID válido del evento 1 del primer item del carrito
        String nombreLocalidad = "VIP"; // Localidad válida
        int cantidadExcedida = 100; // Cantidad mayor a las entradas disponibles

        EditarCarritoDTO editarCarritoDTO = new EditarCarritoDTO(
                idCarrito,
                nombreLocalidad,
                idEvento,
                cantidadExcedida // Cantidad mayor a la disponible
        );

        try {
            carritoServiceImple.editarCarrito(editarCarritoDTO);
            fail("Validacion de editarCarritoCantidadMayorEntradasDisponiblesErrorTest Falló");
        } catch (IllegalArgumentException e) {
            assertEquals("No hay suficientes entradas disponibles en la localidad seleccionada", e.getMessage());
        } catch (Exception e) {
            fail("Validacion de editarCarritoCantidadMayorEntradasDisponiblesErrorTest Falló " + e.getMessage());
        }
    }

}