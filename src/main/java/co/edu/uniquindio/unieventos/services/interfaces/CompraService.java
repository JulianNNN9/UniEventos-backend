package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.compra.ActualizarCompraDTO;
import co.edu.uniquindio.unieventos.dto.compra.CrearCompraDTO;
import co.edu.uniquindio.unieventos.model.Compra;

import java.util.List;

public interface CompraService {

    String crearCompra(CrearCompraDTO crearCompraDTO);

    String actualizarCompra(ActualizarCompraDTO actualizarCompraDTO);

    Compra obtenerCompra(String idCompra);

    List<Compra> obtenerComprasUsuario(String idUsuario);

    String cancelarCompra(String idCompra);

    String realizarCompra(String idCompra);

}
