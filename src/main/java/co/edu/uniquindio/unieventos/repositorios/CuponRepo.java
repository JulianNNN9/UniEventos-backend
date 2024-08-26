package co.edu.uniquindio.unieventos.repositorios;

import co.edu.uniquindio.unieventos.model.Compra;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuponRepo extends MongoRepository<Compra, String> {
}
