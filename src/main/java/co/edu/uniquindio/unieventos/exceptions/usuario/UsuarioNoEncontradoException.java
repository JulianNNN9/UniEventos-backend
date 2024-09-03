package co.edu.uniquindio.unieventos.exceptions.usuario;

import co.edu.uniquindio.unieventos.exceptions.RecursoNoEncontradoException;

public class UsuarioNoEncontradoException extends RecursoNoEncontradoException {
    public UsuarioNoEncontradoException() {
        super("Usuario");
    }
}
