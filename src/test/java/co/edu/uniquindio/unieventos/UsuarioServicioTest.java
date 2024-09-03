package co.edu.uniquindio.unieventos;

import co.edu.uniquindio.unieventos.dto.cuenta.CrearUsuarioDTO;
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
                "1094111999",
                "Juanito Perez",
                "juan@email.com",
                "mipassword",
                "Armenia",
                "Calle 10 # 10-10"
        );
        String codigo = usuarioServicio.crearUsuario(registroClienteDTO);
        assertNotNull(codigo);

    }

}