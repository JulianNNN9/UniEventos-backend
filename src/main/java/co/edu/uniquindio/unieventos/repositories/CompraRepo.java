package co.edu.uniquindio.unieventos.repositories;

import co.edu.uniquindio.unieventos.model.Compra;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompraRepo extends MongoRepository<Compra, String> {

    List<Compra> findAllByIdUsuario(ObjectId idUsuario);

    Optional<Compra> findByCodigoCuponAndIdUsuario(String codigoCupon, String idUsuario);

}
