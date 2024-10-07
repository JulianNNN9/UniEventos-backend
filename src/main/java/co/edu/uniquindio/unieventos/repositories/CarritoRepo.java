package co.edu.uniquindio.unieventos.repositories;

import co.edu.uniquindio.unieventos.model.Carrito;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoRepo extends MongoRepository<Carrito, String> {
    @Query("{ 'idUsuario' : ObjectId(?0) }")
    Optional<Carrito> findByIdUsuario(String idUsuario);
}