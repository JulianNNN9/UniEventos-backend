package co.edu.uniquindio.unieventos.repositories;

import co.edu.uniquindio.unieventos.model.Carrito;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarritoRepo extends MongoRepository<Carrito, String> {
}