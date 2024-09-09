package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.compra.ActualizarCompraDTO;
import co.edu.uniquindio.unieventos.dto.compra.CrearCompraDTO;
import co.edu.uniquindio.unieventos.model.Compra;

import java.util.List;

public interface CompraService {

    String crearCompra(CrearCompraDTO crearCompraDTO) throws Exception;

    Compra obtenerCompra(String idCompra) throws Exception;

    List<Compra> obtenerComprasUsuario(String idUsuario) throws Exception;

    String cancelarCompra(String idCompra) throws Exception;

    String realizarCompra(String idCompra) throws Exception;

}
