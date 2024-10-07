package co.edu.uniquindio.unieventos;

import co.edu.uniquindio.unieventos.dto.FiltrosEventosDTO;
import co.edu.uniquindio.unieventos.dto.evento.*;
import co.edu.uniquindio.unieventos.exceptions.RecursoNoEncontradoException;
import co.edu.uniquindio.unieventos.model.EstadoEvento;
import co.edu.uniquindio.unieventos.model.Localidad;
import co.edu.uniquindio.unieventos.model.TipoEvento;
import co.edu.uniquindio.unieventos.repositories.EventoRepo;
import co.edu.uniquindio.unieventos.services.implementacion.EventoServiceImple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EventoServiceTest {

    @Autowired
    private EventoServiceImple eventoServiceImple;


    @Test
    void testObtenerInformacionEvento() throws Exception {

        //ID DEL EVENTO 1 DEL DATASET
        String idEvento = "66b2c1517f3b340441ffdeb9";
        try{
            InformacionEventoDTO infoEvento = eventoServiceImple.obtenerInformacionEvento(idEvento);
            // Verificar los datos
            assertEquals("Festival de Verano", infoEvento.nombreEvento());
        } catch (Exception e){
            fail("Validacion de testObtenerInformacionEvento Falló "  + e.getMessage());
        }

    }
    @Test
    void testObtenerInformacionEventoIdNoExisteError() throws Exception {

        String idEvento = "123456"; //ID Erroneo del evento
        try{
            eventoServiceImple.obtenerInformacionEvento(idEvento);
            fail("Validacion de testObtenerInformacionEvento Falló ");
        } catch (RecursoNoEncontradoException e){
            assertEquals("Evento no encontrado", e.getMessage());
        } catch (Exception e){
            fail("Validacion de testObtenerInformacionEvento Falló " + e.getMessage());
        }
    }
    @Test
    void testCrearEvento() {
        List<Localidad> localidades = new ArrayList<>();
        localidades.add(new Localidad("LOCALIDAD1", 20000.0, 100, 100));

        CrearEventoDTO crearEventoDTO = new CrearEventoDTO(
                "Concierto de POP",
                "Calle 123",
                "Armenia",
                "Un gran concierto de POP",
                TipoEvento.CONCIERTO,
                LocalDate.now().plusDays(5),
                localidades,
                "imagenPortada.jpg",
                "imagenLocalidades.jpg"
        );

        String resultado = eventoServiceImple.crearEvento(crearEventoDTO);
        assertEquals("Evento creado exitosamente", resultado);
    }
    @Test
    void testEditarEvento() throws Exception {
        //ID del evento 2 del dataset
        String idEvento = "66b2c1517f3b340441ffdeba";
        // Ahora editamos el evento
        EditarEventoDTO editarEventoDTO = new EditarEventoDTO(
                idEvento,
                "Evento Editado",
                "Avenida el caimo calle 11 # 44",
                "Calarca",
                "Nueva descripcion",
                LocalDateTime.now().plusDays(5), // Fecha editada
                null,
                "nuevaImagenPortada.jpg",
                "nuevaImagenLocalidades.jpg",
                EstadoEvento.INACTIVO
        );

        try{
            eventoServiceImple.editarEvento(editarEventoDTO);

            // Verificar que el evento ha sido editado correctamente
            InformacionEventoDTO eventoEditado = eventoServiceImple.obtenerInformacionEvento(idEvento);
            assertEquals("Evento Editado", eventoEditado.nombreEvento());
        } catch (Exception e){
            fail("Validacion de testEditarEvento Falló " + e.getMessage());
        }
    }
    @Test
    void testListarEventos() throws Exception {
        List<ItemEventoDTO>itemEventoDTOList = eventoServiceImple.listarEventos();
        //Deben haber mas 5 de eventos en la coleccion de eventos
        assertTrue(itemEventoDTOList.size() >= 5);
    }
    @Test
    void testNotificarNuevoEvento() throws Exception {

        //Creaamos 2 eventos para la fecha actual y de ayer

        List<Localidad> localidades = new ArrayList<>();
        localidades.add(new Localidad("CENTRO", 30000.0, 50, 50));

        CrearEventoDTO crearEventoDTO = new CrearEventoDTO(
                "Evento de Literatura",
                "Calle 100 Avenida 13",
                "Circasia",
                "Un evento para los apaasionados a los libros",
                TipoEvento.OTRO,
                LocalDate.now().minusDays(30),
                localidades,
                "imagenPortada.jpg",
                "imagenLocalidades.jpg"
        );
        eventoServiceImple.crearEvento(crearEventoDTO);

        localidades = new ArrayList<>();
        localidades.add(new Localidad("PRESIDENCIAL", 70000.0, 50, 50));

        crearEventoDTO = new CrearEventoDTO(
                "Evento de Computacion",
                "Calle 30 Avenida 16",
                "Salento",
                "Te atreves a resolver 30 ejercicios de lógica de programacion?",
                TipoEvento.OTRO,
                LocalDate.now().plusDays(20),
                localidades,
                "imagenPortada.jpg",
                "imagenLocalidades.jpg"
        );
        eventoServiceImple.crearEvento(crearEventoDTO);
        List<NotificacionEventoDTO>notificacionEventoDTOList = eventoServiceImple.notificarNuevoEvento();
        //Deben haber mas 2 eventos en las notificaciones, ya que los 2 que creamos son de la actualidad (fechacreacion)
        assertTrue(notificacionEventoDTOList.size() >=2); //Son 3 contando si se ejecuta el test de crear evento
    }
    @Test
    void testBuscarEventosPorNombre() throws Exception {
        //HAY 2 EVENTOS CON NOMBRE FESTIVAL
        String nombreEvento = "Festival";
        List<ItemEventoDTO>itemEventoDTOList = eventoServiceImple.buscarEventoPorNombre(nombreEvento);
        for (ItemEventoDTO itemEventoDTO: itemEventoDTOList){
            System.out.println(itemEventoDTO);
        }
        assertEquals(2, itemEventoDTOList.size());
    }
    @Test
    public void testFiltrarEvento_ConFiltrosValidos() {
        // Crear datos de prueba
        FiltrosEventosDTO filtros = new FiltrosEventosDTO("Festival", null, "Medellin");

        // Simular la respuesta esperada
        List<ItemEventoDTO> eventosFiltrados = eventoServiceImple.filtrarEvento(filtros);
        for (ItemEventoDTO itemEventoDTO: eventosFiltrados){
            System.out.println(itemEventoDTO);
        }
        //HAY UN EVENTO que contenga el nombre 'Festival' y la Ciudad de 'Medellin' (Evento 1 del dataset)
        assertEquals(1, eventosFiltrados.size());
    }

    @Test
    void testEliminarEvento() throws Exception {
        //ID DEL Evento 5 del Dataset
        String idEvento = "66b2c1517f3b340441ffdebd";
        eventoServiceImple.eliminarEvento(idEvento);
        assertThrows(RecursoNoEncontradoException.class, () -> eventoServiceImple.obtenerEvento(idEvento) );

    }



}
