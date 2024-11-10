package co.edu.uniquindio.unieventos.repositories;

import co.edu.uniquindio.unieventos.model.Compra;
import co.edu.uniquindio.unieventos.model.EstadoCompra;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompraRepo extends MongoRepository<Compra, String> {

    @Query("{ 'usuario.$id': ObjectId(?0) }")
    List<Compra> findAllByIdUsuario(String usuarioId);

    @Query("{ 'usuario.$id': ObjectId(?0), 'estadoCompra': { $ne: ?1 } }")
    List<Compra> findAllByIdUsuarioAndEstadoNoy(String usuarioId, EstadoCompra estadoCompra);

    @Query("{ 'codigoCupon': ?0, 'usuario.$id': ObjectId(?1) }")
    Optional<Compra> findByCodigoCuponAndIdUsuario(String codigoCupon, String idUsuario);
}
