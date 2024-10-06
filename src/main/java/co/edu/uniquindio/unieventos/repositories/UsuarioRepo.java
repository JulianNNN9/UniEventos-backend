package co.edu.uniquindio.unieventos.repositories;

import co.edu.uniquindio.unieventos.model.EstadoUsuario;
import co.edu.uniquindio.unieventos.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepo extends MongoRepository<Usuario, String> {

    Optional<Usuario> findByIdAndEstadoUsuarioNot(String cedula, EstadoUsuario estadoUsuario);

    Optional<Usuario> findByCedulaAndEstadoUsuarioNot(String cedula, EstadoUsuario estadoUsuario);

    Optional<Usuario> findByEmailAndEstadoUsuarioNot(String email, EstadoUsuario estadoUsuario);

    Optional<Usuario> findByEmailAndContraseniaAndEstadoUsuarioNot(String email, String contrasenia, EstadoUsuario estadoUsuario);

    Optional<Usuario> findByCodigoRegistroCodigoAndEstadoUsuarioNot(String codigoActivacion, EstadoUsuario estadoUsuario);
}
