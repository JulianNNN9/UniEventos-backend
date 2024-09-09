package co.edu.uniquindio.unieventos;

import co.edu.uniquindio.unieventos.dto.cuenta.CrearUsuarioDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.EditarUsuarioDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.InformacionUsuarioDTO;
import co.edu.uniquindio.unieventos.services.implementacion.UsuarioServiceImple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UsuarioServicioTest {
    @Autowired
    private UsuarioServiceImple usuarioServicio;

    @Test
    public void registrarClienteTest() throws Exception {
        CrearUsuarioDTO registroClienteDTO = new CrearUsuarioDTO(
                "123",
                "Johan Noe Londoño Salazar",
                "Calle 10 # 10-10",
                "3127761299",
                "ljohannoe@gmail.com",
                "mipassword"
        );
        //Si el usuario se guardó en la base de datos, se le asignó un codigo en MongoDB
        String codigo = usuarioServicio.crearUsuario(registroClienteDTO);
        //Comprobamos que el codigo del usuario no es Null
        assertNotNull(codigo);

    }

    @Test
    public void actualizarTest() throws Exception{
        String idUsuario = "66a2a9aaa8620e3c1c5437be";
        EditarUsuarioDTO editarUsuarioDTO = new EditarUsuarioDTO(
                "12",
                "Pepito perez",
                "Nueva dirección",
                "3012223333"
        );
        //Se edita la informacion del usuario
        usuarioServicio.editarUsuario(editarUsuarioDTO);
        //Obtenemos el Usuario
        InformacionUsuarioDTO informacionUsuarioDTO = usuarioServicio.obtenerInformacionUsuario(idUsuario);
        //Comparamos si la edición de la dirección se realizo correctamente
        assertEquals("Nueva dirección", informacionUsuarioDTO.direccion());
    }
    @Test
    public void eliminarTest() throws Exception{
        String idUsuario = "66a2a9aaa8620e3c1c5437be";
        //Se elimina el usuario
        usuarioServicio.eliminarUsuario(idUsuario);
        //Al intentar encontrar el usuario arroja una excepcion porque no existe
        assertThrows(Exception.class, () -> usuarioServicio.obtenerInformacionUsuario(idUsuario) );
    }


}