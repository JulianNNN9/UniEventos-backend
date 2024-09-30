package co.edu.uniquindio.unieventos.repositories;

import co.edu.uniquindio.unieventos.dto.evento.ItemEventoDTO;
import co.edu.uniquindio.unieventos.dto.evento.NotificacionEventoDTO;
import co.edu.uniquindio.unieventos.model.Evento;
import co.edu.uniquindio.unieventos.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository

public interface EventoRepo extends MongoRepository<Evento, String> {

    // Metodos de filtrado
    // Se hace búsqueda para todas las posibles combinaciones de Nombre, Tipo y/o Ciudad del evento
    // Importante: Se debe seguir al pie de la letra el orden de los filtros
    // Individuales
    @Query(value = "{ 'nombreEvento' : { $regex: '?0' } }", fields = "{ 'nombreEvento' : 1, 'direccionEvento' : 1, 'ciudadEvento' : 1, 'fechaEvento' : 1 }")
    List<ItemEventoDTO> findByNombreEvento(String nombreEvento);

    @Query(value = "{ 'tipoEvento' : ?0 }", fields = "{ 'nombreEvento' : 1, 'direccionEvento' : 1, 'ciudadEvento' : 1, 'fechaEvento' : 1 }")
    List<ItemEventoDTO> findByTipoEvento(String tipoEvento);

    @Query(value = "{ 'ciudadEvento' : ?0 }", fields = "{ 'nombreEvento' : 1, 'direccionEvento' : 1, 'ciudadEvento' : 1, 'fechaEvento' : 1 }")
    List<ItemEventoDTO> findByCiudadEvento(String ciudadEvento);

    // Combinaciones de dos atributos
    @Query(value = "{ 'ciudadEvento' : ?0, 'tipoEvento' : ?1 }", fields = "{ 'nombreEvento' : 1, 'direccionEvento' : 1, 'ciudadEvento' : 1, 'fechaEvento' : 1 }")
    List<ItemEventoDTO> findByCiudadEventoAndTipoEvento(String ciudadEvento, String tipoEvento);

    // Fin metodos de filtrado

    // Metodo para encontrar los nuevos eventos que se crearon ayer y hoy
    @Query(value = "{ 'fechaCreacion' : { $gte: ?0, $lte: ?1 } }", fields = "{ _id:1, 'nombreEvento' : 1}")
    List<NotificacionEventoDTO> findNuevosEventosAyerHoy(LocalDate startDate, LocalDate endDate);
}
