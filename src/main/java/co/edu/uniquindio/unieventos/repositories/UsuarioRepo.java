package co.edu.uniquindio.unieventos.repositories;

import co.edu.uniquindio.unieventos.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface UsuarioRepo extends MongoRepository<Usuario, String> {
}
