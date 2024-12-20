package co.edu.uniquindio.unieventos.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document("usuarios")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    private String cedula;
    private String nombreCompleto;
    private String direccion;
    private String telefono;
    private String email;
    private String contrasenia;
    private Rol rol;
    private EstadoUsuario estadoUsuario;
    private LocalDateTime fechaRegistro;
    private CodigoActivacion codigoActivacion;
    private CodigoRecuperacion codigoRecuperacion;
    private List<Cupon> cuponesUsuario;
    private Boolean primeraCompraRealizada;
    private int fallosInicioSesion;
    private LocalDateTime tiempoBloqueo;
}

