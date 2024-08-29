package co.edu.uniquindio.unieventos;

import co.edu.uniquindio.unieventos.model.Usuario;
import co.edu.uniquindio.unieventos.repositories.UsuarioRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class UsuarioRepoTest {

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Test
    public void agregarUsuarioTest() {

        Usuario usuario = new Usuario();

        usuario.setCedula("dsadsa");
        usuario.setNombreCompleto("Malcom");
        usuario.setEmail("dsadsa@gmail.com");

        Assertions.assertNotNull(usuarioRepo.save(usuario));
    }

    @Test
    public void eliminarUsuarioTest() {
        String id = "dsadsa";
        usuarioRepo.deleteById("111111");
        Optional<Usuario> usuarioOptional = usuarioRepo.findById(id);
        Assertions.assertTrue(usuarioOptional.isEmpty());
    }

    @Test
    public void actualizarUsuarioTest() {

        Optional<Usuario> usuarioOptional = usuarioRepo.findById("1111");

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            usuario.setCedula("48915");

            Usuario usuarioActualizado = usuarioRepo.save(usuario);
            Assertions.assertEquals("48915", usuarioActualizado.getCedula());
        }
    }

    @Test
    public void obtenerUsuarioTest() {

        Optional<Usuario> usuarioOptional = usuarioRepo.findById("22323");

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            System.out.println(usuario);
        }
    }

}