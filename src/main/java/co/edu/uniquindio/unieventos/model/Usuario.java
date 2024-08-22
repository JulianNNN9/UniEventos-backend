package co.edu.uniquindio.unieventos.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document("usuarios")
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class Usuario {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    private String cedula;
    private String nombreCompleto;
    private String direccion;
    private String telefono;
    private  String email;
    private String contrasenia;
    private Rol rol;
    private boolean activa = false;
    private List<Cupon> cupones;
    private List<Compra> compras;

}

enum Rol { CLIENTE, ADMINISTRADOR }