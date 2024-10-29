package co.edu.uniquindio.unieventos.repositories;

import co.edu.uniquindio.unieventos.dto.evento.ItemEventoDTO;
import co.edu.uniquindio.unieventos.dto.evento.NotificacionEventoDTO;
import co.edu.uniquindio.unieventos.model.*;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository

public interface EventoRepo extends MongoRepository<Evento, String> {

    List<Evento> findByFechaEventoAfterAndEstadoEvento(LocalDateTime fecha, EstadoEvento estado);

    // Método para encontrar un evento por su ID, excluyendo los eliminados
    @Query("{ '_id' : ?0, 'estadoEvento' : { $ne: ?1 } }")
    Optional<Evento> findByIdAndEstadoNot(String id, EstadoEvento estadoEvento);

    // Método para encontrar eventos por nombre, tipo y ciudad, excluyendo un estado específico
    @Query(value = "{ $and: [ " +
            "  { 'nombreEvento': { $regex: ?0, $options: 'i' } }, " +  // Filtro por nombre de evento (insensible a mayúsculas/minúsculas)
            "  { 'tipoEvento': ?1 }, " +  // Filtro por tipo de evento
            "  { 'ciudadEvento': { $regex: ?2, $options: 'i' } }, " +  // Filtro por ciudad de evento (insensible a mayúsculas/minúsculas)
            "  { 'estadoEvento': { $ne: ?3 } } " +  // Filtrar por estadoEvento no igual al proporcionado
            "] }",
            fields = "{ 'nombreEvento': 1, 'direccionEvento': 1, 'ciudadEvento': 1, 'fechaEvento': 1 }")
    List<ItemEventoDTO> findByNombreTipoCiudadAndEstadoNot(String nombreEvento, String tipoEvento, String ciudadEvento, EstadoEvento estadoEvento);

    // Método para encontrar eventos creados en un rango de fechas, excluyendo los de un estado específico
    @Query(value = "{ $and: [ " +
            "  { 'fechaCreacion' : { $gte: ?0, $lte: ?1 } }, " +
            "  { 'estadoEvento': { $ne: ?2 } } " +  // Filtrar por estadoEvento no igual al proporcionado
            "] }",
            fields = "{ _id:1, 'nombreEvento' : 1}")
    List<NotificacionEventoDTO> findNuevosEventosAyerHoyAndEstadoNot(LocalDate startDate, LocalDate endDate, EstadoEvento estadoEvento);

    // Método para buscar eventos por nombre, excluyendo eventos de un estado específico
    @Query(value = "{ $and: [ " +
            "  { 'nombreEvento': { $regex: ?0, $options: 'i' } }, " +
            "  { 'estadoEvento': { $ne: ?1 } } " +  // Filtrar por estadoEvento no igual al proporcionado
            "] }",
            fields = "{ 'nombreEvento': 1, 'direccionEvento': 1, 'ciudadEvento': 1, 'fechaEvento': 1 }")
    List<ItemEventoDTO> findByNombreEventoAndEstadoNot(String valorCampoDeBusqueda, EstadoEvento estadoEvento);

    // Método para encontrar eventos por tipo, excluyendo un estado específico
    @Query(value = "{ $and: [ " +
            "  { 'tipoEvento': ?0 }, " +
            "  { 'estadoEvento': { $ne: ?1 } } " +  // Filtrar por estadoEvento no igual al proporcionado
            "] }",
            fields = "{ 'nombreEvento': 1, 'direccionEvento': 1, 'ciudadEvento': 1, 'fechaEvento': 1 }")
    List<ItemEventoDTO> findByTipoEventoAndEstadoNot(String tipoEvento, EstadoEvento estadoEvento);

    // Método para encontrar eventos por ciudad, excluyendo un estado específico
    @Query(value = "{ $and: [ " +
            "  { 'ciudadEvento': { $regex: ?0, $options: 'i' } }, " +  // Filtro por ciudad de evento (insensible a mayúsculas/minúsculas)
            "  { 'estadoEvento': { $ne: ?1 } } " +  // Filtrar por estadoEvento no igual al proporcionado
            "] }",
            fields = "{ 'nombreEvento': 1, 'direccionEvento': 1, 'ciudadEvento': 1, 'fechaEvento': 1 }")
    List<ItemEventoDTO> findByCiudadEventoAndEstadoNot(String ciudadEvento, EstadoEvento estadoEvento);

    // Método para encontrar todos los eventos que no están eliminados
    @Query(value = "{ 'estadoEvento': { $ne: ?0 } }", fields = "{ 'nombreEvento': 1, 'direccionEvento': 1, 'ciudadEvento': 1, 'fechaEvento': 1 }")
    List<ItemEventoDTO> findByEstadoNot(EstadoEvento estadoEvento);

    // Método para encontrar eventos por nombre y tipo, excluyendo un estado específico
    @Query(value = "{ $and: [ " +
            "  { 'nombreEvento': { $regex: ?0, $options: 'i' } }, " +  // Filtro por nombre de evento (insensible a mayúsculas/minúsculas)
            "  { 'tipoEvento': ?1 }, " +
            "  { 'estadoEvento': { $ne: ?2 } } " +  // Filtrar por estadoEvento no igual al proporcionado
            "] }",
            fields = "{ 'nombreEvento': 1, 'direccionEvento': 1, 'ciudadEvento': 1, 'fechaEvento': 1 }")
    List<ItemEventoDTO> findByNombreTipoEventoAndEstadoNot(String nombreEvento, String tipoEvento, EstadoEvento estadoEvento);

    // Método para encontrar eventos por nombre y ciudad, excluyendo un estado específico
    @Query(value = "{ $and: [ " +
            "  { 'nombreEvento': { $regex: ?0, $options: 'i' } }, " +  // Filtro por nombre de evento (insensible a mayúsculas/minúsculas)
            "  { 'ciudadEvento': { $regex: ?1, $options: 'i' } }, " +  // Filtro por ciudad de evento (insensible a mayúsculas/minúsculas)
            "  { 'estadoEvento': { $ne: ?2 } } " +  // Filtrar por estadoEvento no igual al proporcionado
            "] }",
            fields = "{ 'nombreEvento': 1, 'direccionEvento': 1, 'ciudadEvento': 1, 'fechaEvento': 1 }")
    List<ItemEventoDTO> findByNombreCiudadEventoAndEstadoNot(String nombreEvento, String ciudadEvento, EstadoEvento estadoEvento);

    // Método para encontrar eventos por tipo y ciudad, excluyendo un estado específico
    @Query(value = "{ $and: [ " +
            "  { 'tipoEvento': ?0 }, " +
            "  { 'ciudadEvento': { $regex: ?1, $options: 'i' } }, " +  // Filtro por ciudad de evento (insensible a mayúsculas/minúsculas)
            "  { 'estadoEvento': { $ne: ?2 } } " +  // Filtrar por estadoEvento no igual al proporcionado
            "] }",
            fields = "{ 'nombreEvento': 1, 'direccionEvento': 1, 'ciudadEvento': 1, 'fechaEvento': 1 }")
    List<ItemEventoDTO> findByTipoCiudadEventoAndEstadoNot(String tipoEvento, String ciudadEvento, EstadoEvento estadoEvento);
}