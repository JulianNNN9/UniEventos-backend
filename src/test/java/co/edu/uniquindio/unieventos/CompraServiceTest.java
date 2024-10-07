package co.edu.uniquindio.unieventos;

import co.edu.uniquindio.unieventos.dto.compra.CrearCompraDTO;
import co.edu.uniquindio.unieventos.model.Cupon;
import co.edu.uniquindio.unieventos.model.EstadoCupon;
import co.edu.uniquindio.unieventos.model.ItemCompra;
import co.edu.uniquindio.unieventos.model.TipoCupon;
import co.edu.uniquindio.unieventos.services.implementacion.CompraServiceImple;
import co.edu.uniquindio.unieventos.services.implementacion.CuponServiceImple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CompraServiceTest {

    @Autowired
    private CompraServiceImple compraServiceImple;
    @Autowired
    private CuponServiceImple cuponServiceImple;

//    @Test
//    void testCrearCompra_CuponUsadoException() {
//        // Datos de entrada
//        CrearCompraDTO crearCompraDTO = new CrearCompraDTO(
//                "user123",
//                List.of(new ItemCompra("evento1", "VIP", 2)),
//                "cupon123"
//        );
//
//        // Forzamos la situación donde el cupón ha sido usado
//        Cupon cupon = new Cupon();
//        cupon.setTipoCupon(TipoCupon.UNICO);
//        cupon.setEstadoCupon(EstadoCupon.INACTIVO);  // Cupón ya usado
//
//        // Suponiendo que el servicio de cupones retorna este cupón
//        cuponServiceImple.crearCupon(cupon);
//
//        // Verificar que se lanza la excepción
//        Exception exception = assertThrows(CuponUsadoException.class, () -> {
//            compraServiceImple.crearCompra(crearCompraDTO);
//        });
//
//        // Validar el mensaje de la excepción
//        assertEquals("Este cupón ya fue usado por otra persona", exception.getMessage());
//    }
//
//    @Test
//    void testCrearCompra_ValidCuponAndEntradas() throws Exception {
//        // Datos del cupón válido
//        Cupon cupon = new Cupon("DISCOUNT2024", TipoCupon.UNICO, EstadoCupon.ACTIVO);
//        cuponServiceImple.agregarCupon(cupon);
//
//        // Datos del usuario
//        Usuario usuario = new Usuario("1", "john.doe@example.com");
//        usuarioService.agregarUsuario(usuario);
//
//        // Datos del evento y la localidad
//        Localidad localidad = new Localidad("VIP", 100, 200.0);
//        Evento evento = new Evento("1", "Concierto de Rock");
//        evento.agregarLocalidad(localidad);
//        eventoService.agregarEvento(evento);
//
//        // Crear una compra válida
//        List<ItemCompra> itemsCompra = new ArrayList<>();
//        itemsCompra.add(new ItemCompra("1", "VIP", 2));
//
//        CrearCompraDTO crearCompraDTO = new CrearCompraDTO("1", itemsCompra, "DISCOUNT2024");
//
//        // Ejecutar el método
//        String idCompra = compraServiceImple.crearCompra(crearCompraDTO);
//
//        // Verificar los resultados
//        assertNotNull(idCompra, "El ID de la compra no debe ser nulo");
//        Compra compra = compraRepo.obtenerCompra(idCompra);
//        assertEquals(1, compra.getItemsCompra().size(), "La compra debe contener los items de la lista");
//        assertEquals("DISCOUNT2024", compra.getCodigoCupon(), "El cupón debe coincidir");
//    }
//    @Test
//    void testCrearCompra_EntradasInsuficientesException() throws Exception {
//        // Datos de entrada
//        CrearCompraDTO crearCompraDTO = new CrearCompraDTO(
//                "user123",
//                List.of(new ItemCompra("evento1", "VIP", 10)),  // Pidiendo 10 entradas
//                "cupon123"
//        );
//
//        // Evento con localidad que tiene menos entradas disponibles
//        Evento evento = new Evento();
//        Localidad localidad = new Localidad("VIP", 5);  // Solo hay 5 entradas
//        evento.setLocalidades(List.of(localidad));
//
//        // Suponiendo que el servicio de eventos retorna este evento
//        eventoService.guardarEvento(evento);
//
//        // Verificar que se lanza la excepción de entradas insuficientes
//        Exception exception = assertThrows(EntradasInsuficientesException.class, () -> {
//            compraServiceImple.crearCompra(crearCompraDTO);
//        });
//
//        // Validar el mensaje de la excepción
//        assertEquals("No hay suficientes entradas restantes para la localidad: VIP", exception.getMessage());
//    }
//    @Test
//    void testCancelarCompra() throws Exception {
//        // Datos del usuario
//        Usuario usuario = new Usuario("1", "jane.doe@example.com");
//        usuarioService.agregarUsuario(usuario);
//
//        // Datos del evento y la localidad
//        Localidad localidad = new Localidad("VIP", 50, 100.0);
//        Evento evento = new Evento("1", "Concierto de Pop");
//        evento.agregarLocalidad(localidad);
//        eventoService.agregarEvento(evento);
//
//        // Crear una compra inicial
//        List<ItemCompra> itemsCompra = new ArrayList<>();
//        itemsCompra.add(new ItemCompra("1", "VIP", 3));
//        Compra compra = new Compra("1", "1", itemsCompra, EstadoCompra.PENDIENTE, "CUPON2024");
//        compraRepo.guardarCompra(compra);
//
//        // Ejecutar el método cancelarCompra
//        String resultado = compraServiceImple.cancelarCompra(compra.getId());
//
//        // Verificar que la compra fue cancelada
//        assertEquals("Compra cancelada exitosamente,", resultado);
//        Compra compraCancelada = compraRepo.obtenerCompra(compra.getId());
//        assertEquals(EstadoCompra.CANCELADA, compraCancelada.getEstado(), "La compra debe estar cancelada");
//
//        // Verificar que las entradas fueron devueltas a la localidad
//        Evento eventoActualizado = eventoService.obtenerEvento("1");
//        Localidad localidadActualizada = eventoActualizado.obtenerLocalidad("VIP");
//        assertEquals(50, localidadActualizada.getEntradasRestantes(), "Las entradas deben haber sido devueltas a la localidad");
//    }
//    @Test
//    void testCancelarCompra_RecursoNoEncontradoException() {
//        // ID de compra inexistente
//        String idCompra = "compraNoExistente";
//
//        // Verificar que se lanza la excepción de compra no encontrada
//        Exception exception = assertThrows(RecursoNoEncontradoException.class, () -> {
//            compraServiceImple.cancelarCompra(idCompra);
//        });
//
//        // Validar el mensaje de la excepción
//        assertEquals("Compra no encontrada con el ID: " + idCompra, exception.getMessage());
//    }
}
