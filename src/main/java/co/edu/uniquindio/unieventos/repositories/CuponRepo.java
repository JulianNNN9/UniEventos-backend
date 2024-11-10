package co.edu.uniquindio.unieventos.repositories;

import co.edu.uniquindio.unieventos.model.Cupon;
import co.edu.uniquindio.unieventos.model.EstadoCupon;
import co.edu.uniquindio.unieventos.model.EstadoEvento;
import co.edu.uniquindio.unieventos.model.Evento;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CuponRepo extends MongoRepository<Cupon, String> {
    // Método para encontrar todos los eventos que no están eliminados
    @Query(value = "{ 'estadoCupon': { $ne: ?0 } }")
    List<Cupon> findByEstadoNot(EstadoCupon estadoCupon);

    @Query("{ 'codigo' : ?0, 'estadoCupon' : { $ne: ?1 } }")
    Optional<Cupon> findByCodigoAndEstadoNot(String codigo, EstadoCupon estadoCupon);

    @Query("{ 'usuario.$id': ObjectId(?0), 'estadoCupon': { $ne: ?1 } }")
    List<Cupon> findByUsuarioIdAndEstadoNot(String usuarioId, EstadoCupon estadoCupon);

    @Query("{ 'codigo' : ?0, 'usuario.$id' : ObjectId(?1), 'estadoCupon' : { $ne: ?2 } }")
    Optional<Cupon> findByCodigoAndIdUsuarioAndEstadoNot(String codigo, String idUsuario, EstadoCupon estadoCupon);

    @Query("{ '_id' : ObjectId(?0), 'estadoCupon' : { $ne: ?1 } }")
    Optional<Cupon> findByIdAndEstadoNot(String id, EstadoCupon estadoCupon);
}
