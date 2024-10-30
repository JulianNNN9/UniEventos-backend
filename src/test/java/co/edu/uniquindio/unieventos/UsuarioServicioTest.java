package co.edu.uniquindio.unieventos;

import co.edu.uniquindio.unieventos.dto.cuenta.*;
import co.edu.uniquindio.unieventos.exceptions.*;
import co.edu.uniquindio.unieventos.model.CodigoRecuperacion;
import co.edu.uniquindio.unieventos.model.EstadoUsuario;
import co.edu.uniquindio.unieventos.model.Usuario;
import co.edu.uniquindio.unieventos.repositories.UsuarioRepo;
import co.edu.uniquindio.unieventos.services.implementacion.UsuarioServiceImple;
import org.bson.assertions.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


/*
IMPORTANTE: Se necesita cargar el Script de datase.js en la base de datos para poder realizar correctamente las
            Pruebas
 */
@SpringBootTest
public class UsuarioServicioTest {
    @Autowired
    private UsuarioServiceImple usuarioServicio;

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Test
    public void enviarCodigoActivacionCuentaTest() throws RecursoNoEncontradoException {
        //USUARIO 1 DEL DATASET
        String idUsuario = "66b2a9aaa8620e3c1c5437be";

        //Necesitamos el correo del usuario para poder enviar el codigo de activacion
        Usuario usuario = usuarioServicio.obtenerUsuario(idUsuario);


        assertDoesNotThrow(() -> usuarioServicio.enviarCodigoActivacionCuenta(usuario.getEmail()));

    }

    @Test
    public void registrarClienteCorrectoTest() {
        //SE TIENE que colocar un correo valido porque al crearse el usuario
        //se envia el codigo de activacion de la cuenta
        CrearUsuarioDTO registroClienteDTO = new CrearUsuarioDTO(
                "1",
                "Julian Admin",
                "Calle 10 # 10-10",
                "1",
                "JA@gmail.com",
                "0000"
        );
        //Si el usuario se guardó en la base de datos, se le asignó un codigo en MongoDB
        try{
            String id = usuarioServicio.crearUsuario(registroClienteDTO);
            //Comprobamos que el codigo del usuario no es Null
            assertNotNull(id);
        }catch (Exception e){
            Assertions.fail("Validacion de Crear Usuario Falló" + e.getMessage());
        }

    }
    @Test
    public void registrarClienteCedulaExisteErrorTest() {

        //USUARIO 1 del DataSet
        String cedulaRepetida = "1223444";
        CrearUsuarioDTO registroClienteDTO1 = new CrearUsuarioDTO(
                cedulaRepetida, // Cédula repetida
                "Carlos Perez",
                "Calle 10 # 22-12",
                "3013334444",
                "carlosperez2@email.com",
                "hashed_password_1"
        );
        try{
            String id = usuarioServicio.crearUsuario(registroClienteDTO1);
            Assertions.fail("Validacion de registrarClienteCedulaExisteErrorTest Falló");
        } catch (RecursoEncontradoException e) {
            assertEquals("La cedula ya está en uso", e.getMessage());
        } catch (Exception e){
            Assertions.fail("Validacion de registrarClienteCedulaExisteErrorTest" + e.getMessage());
        }

    }
    @Test
    public void registrarClienteEmailExisteErrorTest() {

        //USUARIO 1 del DataSet
        String emailRepetido = "carlosperez@email.com";
        CrearUsuarioDTO registroClienteDTO1 = new CrearUsuarioDTO(
                "12234441",
                "Carlos Perez",
                "Calle 10 # 22-12",
                "3013334444",
                emailRepetido, // Email Repetido
                "hashed_password_1"
        );
        try{
            String id = usuarioServicio.crearUsuario(registroClienteDTO1);
            Assertions.fail("Validacion de registrarClienteEmailExisteError Falló");
        } catch (RecursoEncontradoException e) {
            assertEquals("Este email ya está en uso", e.getMessage());
        } catch (Exception e){
            Assertions.fail("Validacion de registrarClienteEmailExisteError Falló" + e.getMessage());
        }

    }

    @Test
    public void obtenerInformacionUsuarioCorrectoTest() {
        //USUARIO 5 del DataSet
        String idUsuario = "66b2c1517f3b340441ffdeb3";
        //Obtenemos la informacion del Usuario
        try{
            InformacionUsuarioDTO informacionUsuarioDTO = usuarioServicio.obtenerInformacionUsuario(idUsuario);
            //Comparamos si la informacion obtenida del usuario es correcta meidnate su cedula
            assertEquals("1223448", informacionUsuarioDTO.cedula());
        } catch (RecursoNoEncontradoException e){
            Assertions.fail("Validacion de obtenerInformacionUsuarioCorrectoTest Falló" + e.getMessage());
        } catch (Exception e){
            Assertions.fail("Validacion de obtenerInformacionUsuarioCorrectoTest Falló" + e.getMessage());
        }

    }
    @Test
    public void obtenerInformacionUsuarioErrorTest() {
        //Id que no existe
        String idUsuario = "1";
        try{
            InformacionUsuarioDTO informacionUsuarioDTO = usuarioServicio.obtenerInformacionUsuario(idUsuario);
            Assertions.fail("Validacion de obtenerInformacionUsuarioErrorTest Falló ");
        } catch (RecursoNoEncontradoException e){
            assertEquals("Usuario no encontrado", e.getMessage());
        } catch (Exception e){
            Assertions.fail("Validacion de obtenerInformacionUsuarioErrorTest Falló " + e.getMessage());
        }

    }

    @Test
    public void editarUsuarioCorrectoTest() {
        //USUARIO 2 del DataSet
        String idUsuario = "66b2b14dd9219911cd34f2c1";
        EditarUsuarioDTO editarUsuarioDTO = new EditarUsuarioDTO(
                idUsuario,
                "Pepito perez",
                "Nueva dirección #0 Casa 0",
                "322222222"
        );
        try{
            //Se edita la informacion del usuario
            usuarioServicio.editarUsuario(editarUsuarioDTO);
            //Obtenemos el Usuario
            InformacionUsuarioDTO informacionUsuarioDTO = usuarioServicio.obtenerInformacionUsuario(idUsuario);
            //Comparamos si la edición de la dirección se realizo correctamente
            assertEquals("Nueva dirección #0 Casa 0", informacionUsuarioDTO.direccion());
        } catch (RecursoNoEncontradoException e){
            Assertions.fail("Validacion de editarUsuarioCorrectoTest Falló " + e.getMessage());
        } catch (Exception e){
            Assertions.fail("Validacion de editarUsuarioCorrectoTest Falló " + e.getMessage());
        }
    }
    @Test
    public void eliminarUsuarioTest() throws RecursoNoEncontradoException{
        //USUARIO 3 del DataSet
        String idUsuario = "66b2c1517f3b340441ffdeb1";
        //Se elimina el usuario
        usuarioServicio.eliminarUsuario(idUsuario);
        //Al intentar encontrar el usuario arroja una excepcion porque no existe
        assertThrows(RecursoNoEncontradoException.class, () -> usuarioServicio.obtenerInformacionUsuario(idUsuario) );
    }

    @Test
    public void enviarCodigoRecuperacionCuentaTest() throws RecursoNoEncontradoException {
        //USUARIO 1 DEL DATASET
        String idUsuario = "66b2a9aaa8620e3c1c5437be";

        //Necesitamos el correo del usuario para poder enviar el codigo de activacion
        Usuario usuario = usuarioServicio.obtenerUsuario(idUsuario);

        assertDoesNotThrow(() -> usuarioServicio.enviarCodigoRecuperacionCuenta(usuario.getEmail()));

    }
    @Test
    public void recuperarContraseniaTest() throws Exception {
        // ID del USUARIO 4 del DataSet
        String idUsuario = "66b2c1517f3b340441ffdeb2";
        Usuario usuario = usuarioServicio.obtenerUsuario(idUsuario);

        //Simular que el Usuario ya le dio a RecuperarContraseña (enviarCodigoRecuperacionCuenta)
        // por ende se asgino un nuevo codigo de recuperacion al usuario
        CodigoRecuperacion codigoRecuperacion = CodigoRecuperacion.builder()
                .codigo("123456")
                .fechaCreacion(LocalDateTime.now())
                .build();
        usuario.setCodigoRecuperacion(codigoRecuperacion);

        //Guardamos el usuario con el nuevo codigo de recuperacion
        usuarioRepo.save(usuario);

        Thread.sleep(1000);

        RecuperarContraseniaDTO recuperarContraseniaDTO = new RecuperarContraseniaDTO(
                usuario.getEmail(), // Correo del Usuario 4
                "123456", // Código de verificación
                "nuevaPassword", // Nueva contraseña
                "nuevaPassword" // Confirmación de nueva contraseña
        );

        assertDoesNotThrow(() -> usuarioServicio.recuperarContrasenia(recuperarContraseniaDTO));
    }
    @Test
    public void recuperarContraseniaEmailNoExisteErrorTest() throws Exception {

        String correoNoExiste = "correonoexiste@gmail.com";
        RecuperarContraseniaDTO recuperarContraseniaDTO = new RecuperarContraseniaDTO(
                correoNoExiste, // ID del Usuario 4
                "123456", // Código de verificación
                "nuevaPassword", // Nueva contraseña
                "nuevaPassword2" // Confirmación de nueva contraseña DIFERENTE
        );

        try{
            usuarioServicio.recuperarContrasenia(recuperarContraseniaDTO);
            Assertions.fail("Validacion de recuperarContraseniaEmailNoExisteErrorTest Falló");
        }catch (RecursoNoEncontradoException e){
            assertEquals("Email no encontrado", e.getMessage());
        }catch (Exception e){
            Assertions.fail("Validacion de recuperarContraseniaEmailNoExisteErrorTest Falló " + e.getMessage());
        }
    }
    @Test
    public void recuperarContraseniaNoCoincidenErrorTest() throws Exception {

        RecuperarContraseniaDTO recuperarContraseniaDTO = new RecuperarContraseniaDTO(
                "anaruiz@email.com", // ID del Usuario 4
                "123456", // Código de verificación
                "nuevaPassword", // Nueva contraseña
                "nuevaPassword2" // Confirmación de nueva contraseña DIFERENTE
        );

        try{
            usuarioServicio.recuperarContrasenia(recuperarContraseniaDTO);
            Assertions.fail("Validacion de recuperarContraseniaNoCoincidenErrorTest Falló");
        }catch (ContraseniaNoCoincidenException e){
            assertEquals("Las contraseñas no coindicen", e.getMessage());
        }catch (Exception e){
            Assertions.fail("Validacion de recuperarContraseniaNoCoincidenErrorTest Falló " + e.getMessage());
        }
    }
    @Test
    public void recuperarContraseniaCodigoExpiradoErrorTest() throws Exception {
        // ID del USUARIO 4 del DataSet
        String idUsuario = "66b2c1517f3b340441ffdeb2";
        Usuario usuario = usuarioServicio.obtenerUsuario(idUsuario);

        //Simular que el Usuario ya le dio a RecuperarContraseña (enviarCodigoRecuperacionCuenta)
        // por ende se asgino un nuevo codigo de recuperacion al usuario
        CodigoRecuperacion codigoRecuperacion = CodigoRecuperacion.builder()
                .codigo("123456")
                .fechaCreacion(LocalDateTime.now().minusMinutes(15)) //Restar 15 Minutos para expirar el codigo
                .build();
        usuario.setCodigoRecuperacion(codigoRecuperacion);

        //Guardamos el usuario con el nuevo codigo de recuperacion
        usuarioRepo.save(usuario);

        Thread.sleep(1000);

        RecuperarContraseniaDTO recuperarContraseniaDTO = new RecuperarContraseniaDTO(
                usuario.getEmail(), // ID del Usuario 4
                "123456", // Código de verificación
                "nuevaPassword", // Nueva contraseña
                "nuevaPassword" // Confirmación de nueva contraseña
        );

        try{
            usuarioServicio.recuperarContrasenia(recuperarContraseniaDTO);
            Assertions.fail("Validacion de recuperarContraseniaCodigoExpiradoErrorTest Falló");
        }catch (CodigoExpiradoException e){
            assertEquals("El código expiró", e.getMessage());
        }catch (Exception e){
            Assertions.fail("Validacion de recuperarContraseniaCodigoExpiradoErrorTest Falló " + e.getMessage());
        }
    }
    @Test
    public void recuperarContraseniaCodigoIncorrectoErrorTest() throws Exception {
        // ID del USUARIO 4 del DataSet
        String idUsuario = "66b2c1517f3b340441ffdeb2";
        Usuario usuario = usuarioServicio.obtenerUsuario(idUsuario);

        //Simular que el Usuario ya le dio a RecuperarContraseña (enviarCodigoRecuperacionCuenta)
        // por ende se asgino un nuevo codigo de recuperacion al usuario
        CodigoRecuperacion codigoRecuperacion = CodigoRecuperacion.builder()
                .codigo("123456")
                .fechaCreacion(LocalDateTime.now())
                .build();
        usuario.setCodigoRecuperacion(codigoRecuperacion);

        //Guardamos el usuario con el nuevo codigo de recuperacion
        usuarioRepo.save(usuario);

        Thread.sleep(1000);

        RecuperarContraseniaDTO recuperarContraseniaDTO = new RecuperarContraseniaDTO(
                usuario.getEmail(), // ID del Usuario 4
                "111111", // Código de verificación Incorrecto
                "nuevaPassword", // Nueva contraseña
                "nuevaPassword" // Confirmación de nueva contraseña
        );

        try{
            usuarioServicio.recuperarContrasenia(recuperarContraseniaDTO);
            Assertions.fail("Validacion de recuperarContraseniaCodigoIncorrectoErrorTest Falló");
        }catch (CodigoInvalidoException e){
            assertEquals("El código es incorrecto", e.getMessage());
        }catch (Exception e){
            Assertions.fail("Validacion de recuperarContraseniaCodigoIncorrectoErrorTest Falló " + e.getMessage());
        }
    }
    @Test
    public void cambiarContraseniaCorrectaTest() throws Exception {
        //SE necesita crear un usuario para que su contraseña se encripte en la base de datos
        String contrasenia = "micontraseña";

        CrearUsuarioDTO registroClienteDTO = new CrearUsuarioDTO(
                "1111111111",
                "Carlos Alberto",
                "Calle 50 # 10-20",
                "3121212121",
                "johan.71000@gmail.com",
                contrasenia
        );
        String id = usuarioServicio.crearUsuario(registroClienteDTO);

        CambiarContraseniaDTO cambiarContraseniaDTO = new CambiarContraseniaDTO(
                id, // ID del usuario
                contrasenia, // Contraseña actual
                "nuevaPassword", // Nueva contraseña
                "nuevaPassword" // Confirmar nueva contraseña
        );
        assertDoesNotThrow(() -> usuarioServicio.cambiarContrasenia(cambiarContraseniaDTO));
    }
    @Test
    public void cambiarContraseniaNoCoincidenTest() {
        //Se requiere dar un ID de un Usuario que ya esté en la base de datos, no importa si la contraseña no esta
        //encriptada porque no llega a ingresar al metodo que valida la contraseña antigua con la actual encriptada
        //de la base de datos

        //ID USUARIO 2 del DataSet
        String idUsuario = "66b2b14dd9219911cd34f2c1";

        CambiarContraseniaDTO cambiarContraseniaDTO = new CambiarContraseniaDTO(
                idUsuario, // ID del usuario
                "cualquierContraseña", // Contraseña actual
                "nuevaPassword", // Nueva contraseña
                "contraseniaDiferente" // Confirmar nueva contraseña (DIFERENTE
        );
        try{
            usuarioServicio.cambiarContrasenia(cambiarContraseniaDTO);
            Assertions.fail("Validacion de cambiarContraseniaNoCoincidenTest Falló");
        }catch (ContraseniaNoCoincidenException e){
            assertEquals("Las contraseñas no coindicen", e.getMessage());
        }catch (Exception e){
            Assertions.fail("Validacion de cambiarContraseniaNoCoincidenTest Falló " + e.getMessage());
        }
    }
    @Test
    public void cambiarContraseniaIncorrectaTest() throws Exception {
        //SE necesita crear un usuario para que su contraseña se encripte en la base de datos
        String contrasenia = "micontraseña";

        CrearUsuarioDTO registroClienteDTO = new CrearUsuarioDTO(
                "2222222222",
                "Juan Sebastian Orozco",
                "Calle 20 # 100-200",
                "3123333333",
                "juliana.hoyosg@email.com",
                contrasenia
        );
        String id = usuarioServicio.crearUsuario(registroClienteDTO);

        CambiarContraseniaDTO cambiarContraseniaDTO = new CambiarContraseniaDTO(
                id, // ID del usuario
                "Contraseña_Incorrecta", // Contraseña actual INCORRECTA
                "nuevaPassword", // Nueva contraseña
                "nuevaPassword" // Confirmar nueva contraseña
        );

        try{
            usuarioServicio.cambiarContrasenia(cambiarContraseniaDTO);
            Assertions.fail("Validacion de cambiarContraseniaIncorrectaTest Falló");
        }catch (ContraseniaIncorrectaException e){
            assertEquals("La contraseña es incorrecta", e.getMessage());
        }catch (Exception e){
            Assertions.fail("Validacion de cambiarContraseniaIncorrectaTest Falló " + e.getMessage());
        }
    }
    @Test
    public void iniciarSesionTest() throws Exception {
        //Crear Usuario para encriptar la contraseña:
        CrearUsuarioDTO registroClienteDTO = new CrearUsuarioDTO(
                "3333333333",
                "Julian Andres Hoyoz",
                "Calle 1 # 2-4",
                "3444444444",
                "otroemail@gmail.com",
                "mipassword"
        );
        String id = usuarioServicio.crearUsuario(registroClienteDTO);
        //Obtenemos el usuario para cambiarle el estado a ACTIVO
        Usuario usuario = usuarioServicio.obtenerUsuario(id);
        usuario.setEstadoUsuario(EstadoUsuario.ACTIVA);
        usuarioRepo.save(usuario);

        IniciarSesionDTO iniciarSesionDTO = new IniciarSesionDTO(
                "otroemail@gmail.com", // Email
                "mipassword" // Contraseña
        );

        assertDoesNotThrow(() -> usuarioServicio.iniciarSesion(iniciarSesionDTO));
        // Comprobamos que el usuario pueda iniciar sesión correctamente.
    }
    @Test
    public void iniciarSesionCuentaInactivaErrorTest() throws Exception {
        //ID USUARIO 1 del DATASET
        String id = "66b2a9aaa8620e3c1c5437be";
        //USUARIO con estado INACTIVA
        Usuario usuario = usuarioServicio.obtenerUsuario(id);

        IniciarSesionDTO iniciarSesionDTO = new IniciarSesionDTO(
                usuario.getEmail(), // Email
                usuario.getContrasenia() // Contraseña
        );
        try{
            usuarioServicio.iniciarSesion(iniciarSesionDTO);
            Assertions.fail("Validacion de iniciarSesionCuentaInactivaErrorTest Falló");
        }catch (CuentaInactivaEliminadaException e){
            assertEquals("Esta cuenta aún no ha sido activada", e.getMessage());
        }catch (Exception e){
            Assertions.fail("Validacion de iniciarSesionCuentaInactivaErrorTest Falló " + e.getMessage());
        }
    }
    @Test
    public void iniciarSesionContraseniaIncorrectaErrorTest() throws Exception {
        //Crear Usuario para encriptar la contraseña:
        String email = "nicolasemail@email.com";
        String contrasenia = "mipassword";

        CrearUsuarioDTO registroClienteDTO = new CrearUsuarioDTO(
                "3434343434",
                "Nicolas Jurado",
                "Calle 25 # 5-6",
                "32783111",
                email,
                contrasenia
        );
        String id = usuarioServicio.crearUsuario(registroClienteDTO);        //USUARIO con estado INACTIVA
        //Obtenemos el usuario para cambiarle el estadoa  ACTIVA para que
        //no entre a la excepcion de que no esta activada
        Usuario usuario = usuarioServicio.obtenerUsuario(id);
        usuario.setEstadoUsuario(EstadoUsuario.ACTIVA);
        usuarioRepo.save(usuario);

        IniciarSesionDTO iniciarSesionDTO = new IniciarSesionDTO(
                email, // Email
                "ContraseñaIncorrecta"// ContraseñaIncorrecta
        );
        try{
            usuarioServicio.iniciarSesion(iniciarSesionDTO);
            Assertions.fail("Validacion de iniciarSesionContraseniaIncorrectaErrorTest Falló");
        }catch (ContraseniaIncorrectaException e){
            assertEquals("La contraseña es incorrecta", e.getMessage());
        }catch (Exception e){
            Assertions.fail("Validacion de iniciarSesionContraseniaIncorrectaErrorTest Falló " + e.getMessage());
        }
    }
    @Test
    public void iniciarSesionCuentaBloqueadaErrorTest() throws Exception {
        //Crear Usuario para encriptar la contraseña:
        String email = "pepitoemail@email.com";
        String contrasenia = "mipassword";

        CrearUsuarioDTO registroClienteDTO = new CrearUsuarioDTO(
                "1234121212",
                "Pepito Dias",
                "Calle 78 # 4-1",
                "324998822",
                email,
                contrasenia
        );
        String id = usuarioServicio.crearUsuario(registroClienteDTO);        //USUARIO con estado INACTIVA
        //Obtenemos el usuario para cambiarle el estadoa  ACTIVA para que
        //no entre a la excepcion de que no esta activada, ademas setiamos el # de fallos al maximo (5)
        //y agregamos el tiempo de bloqueo
        Usuario usuario = usuarioServicio.obtenerUsuario(id);
        usuario.setEstadoUsuario(EstadoUsuario.ACTIVA);
        usuario.setFallosInicioSesion(5);
        usuario.setTiempoBloqueo(LocalDateTime.now().plusMinutes(5));

        usuarioRepo.save(usuario);

        IniciarSesionDTO iniciarSesionDTO = new IniciarSesionDTO(
                email,
                contrasenia
        );
        try{
            usuarioServicio.iniciarSesion(iniciarSesionDTO);
            Assertions.fail("Validacion de iniciarSesionCuentaBloqueadaErrorTest Falló");
        }catch (CuentaBloqueadaException e){
            assertEquals("La cuenta se encuentra bloqueada por demasiados intentos, espere 5 minutos", e.getMessage());
        }catch (Exception e){
            Assertions.fail("Validacion de iniciarSesionCuentaBloqueadaErrorTest Falló " + e.getMessage());
        }
    }
}