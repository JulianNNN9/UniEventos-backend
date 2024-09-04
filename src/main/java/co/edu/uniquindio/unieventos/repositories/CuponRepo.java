package co.edu.uniquindio.unieventos.repositories;

import co.edu.uniquindio.unieventos.model.Cupon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CuponRepo extends MongoRepository<Cupon, String> {
    Optional<Cupon> findByCodigo(String codigo);
}
