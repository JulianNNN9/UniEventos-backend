package co.edu.uniquindio.unieventos.repositories;

import co.edu.uniquindio.unieventos.model.Cupon;
import co.edu.uniquindio.unieventos.model.EstadoCupon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CuponRepo extends MongoRepository<Cupon, String> {
    @Query("{ 'codigo' : ?0, 'estadoCupon' : { $ne: ?1 } }")
    Optional<Cupon> findByCodigoAndEstadoNot(String codigo, EstadoCupon estadoCupon);

    @Query("{ '_id' : ?0, 'estadoCupon' : { $ne: ?1 } }")
    Optional<Cupon> findByIdAndEstadoNot(String id, EstadoCupon estadoCupon);
}
