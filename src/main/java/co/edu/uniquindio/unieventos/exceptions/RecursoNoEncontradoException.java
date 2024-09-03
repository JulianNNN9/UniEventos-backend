package co.edu.uniquindio.unieventos.exceptions;

public class RecursoNoEncontradoException extends Exception {

    public RecursoNoEncontradoException(String message) {
        super(String.format("%s no encontrado",message));
    }

}
