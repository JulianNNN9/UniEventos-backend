package co.edu.uniquindio.unieventos.repositories;

import co.edu.uniquindio.unieventos.model.Evento;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface EventoRepo extends MongoRepository<Evento, String> {
}
