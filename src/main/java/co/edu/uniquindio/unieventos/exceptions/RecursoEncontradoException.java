package co.edu.uniquindio.unieventos.exceptions;

public class RecursoEncontradoException extends Exception {

    public RecursoEncontradoException(String message) {
        super(String.format("El %s ya existe",message));
    }

}
