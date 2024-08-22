package co.edu.uniquindio.unieventos.repositorios;

import co.edu.uniquindio.unieventos.model.Reporte;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ReporteRepo extends MongoRepository<Reporte, String> {
}
