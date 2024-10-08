package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.compra.CrearCompraDTO;
import co.edu.uniquindio.unieventos.exceptions.EntradasInsuficientesException;
import co.edu.uniquindio.unieventos.exceptions.RecursoEncontradoException;
import co.edu.uniquindio.unieventos.exceptions.RecursoNoEncontradoException;
import co.edu.uniquindio.unieventos.model.Compra;
import java.util.Map;
import com.mercadopago.resources.preference.Preference;
import java.util.List;

public interface CompraService {

    String crearCompra(CrearCompraDTO crearCompraDTO) throws Exception;

    Compra obtenerCompra(String idCompra) throws Exception;

    List<Compra> obtenerComprasUsuario(String idUsuario) throws Exception;

    String cancelarCompra(String idCompra) throws Exception;

    Preference realizarPago(String idOrden) throws Exception;

    void recibirNotificacionMercadoPago(Map<String, Object> requestBody);
}
