package co.edu.uniquindio.unieventos.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document("usuarios")
@NoArgsConstructor
@Data
@SuperBuilder
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
    private CodigoValidacion codigoRegistro;
    private CodigoValidacion codigoRecuperacionContrasenia;

}

