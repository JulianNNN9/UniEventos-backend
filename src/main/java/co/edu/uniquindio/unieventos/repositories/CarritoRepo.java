package co.edu.uniquindio.unieventos.repositories;

import co.edu.uniquindio.unieventos.model.Carrito;
import co.edu.uniquindio.unieventos.model.Compra;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoRepo extends MongoRepository<Carrito, String> {
}
