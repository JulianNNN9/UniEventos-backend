package co.edu.uniquindio.unieventos.exceptions.usuario;

public class ContraseniaNoCoincidenException extends Exception {
    public ContraseniaNoCoincidenException() {
        super("Las contraseñas no coinciden");
    }
}
