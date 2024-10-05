package co.edu.uniquindio.unieventos.repositories;

import co.edu.uniquindio.unieventos.dto.evento.ItemEventoDTO;
import co.edu.uniquindio.unieventos.dto.evento.NotificacionEventoDTO;
import co.edu.uniquindio.unieventos.model.Evento;
import co.edu.uniquindio.unieventos.model.TipoEvento;
import co.edu.uniquindio.unieventos.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository

public interface EventoRepo extends MongoRepository<Evento, String> {

    // Metodos de filtrado, puede recibir nulos
    @Query(value = "{ $and: [ " +
            "  { $or: [ { 'nombreEvento': { $regex: ?0, $options: 'i' } }, { ?0: null }, { ?0: '' } ] }, " +
            "  { $or: [ { 'tipoEvento': ?1 }, { ?1: null }, { ?1: '' } ] }, " +
            "  { $or: [ { 'ciudadEvento': ?2 }, { ?2: null }, { ?2: '' } ] } " +
            "] }",
            fields = "{ 'nombreEvento': 1, 'direccionEvento': 1, 'ciudadEvento': 1, 'fechaEvento': 1 }")
    List<ItemEventoDTO> findByNombreTipoCiudad(String nombreEvento, String tipoEvento, String ciudadEvento);
    // Fin metodos de filtrado

    // Metodo para encontrar los nuevos eventos que se crearon ayer y hoy
    @Query(value = "{ 'fechaCreacion' : { $gte: ?0, $lte: ?1 } }", fields = "{ _id:1, 'nombreEvento' : 1}")
    List<NotificacionEventoDTO> findNuevosEventosAyerHoy(LocalDate startDate, LocalDate endDate);


    @Query(value = "{ 'nombreEvento': { $regex: ?0, $options: 'i' } }",
            fields = "{ 'nombreEvento': 1, 'direccionEvento': 1, 'ciudadEvento': 1, 'fechaEvento': 1 }")
    List<ItemEventoDTO> findByNombreEvento(String valorCampoDeBusqueda);
}
