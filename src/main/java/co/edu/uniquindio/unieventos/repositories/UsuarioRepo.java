package co.edu.uniquindio.unieventos.repositories;

import co.edu.uniquindio.unieventos.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepo extends MongoRepository<Usuario, String> {

    Optional<Usuario> findByCedula(String cedula);

    Optional<Usuario> findByEmail(String email);
}
