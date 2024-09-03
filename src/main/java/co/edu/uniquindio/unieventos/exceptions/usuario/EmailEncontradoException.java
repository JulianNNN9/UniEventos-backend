package co.edu.uniquindio.unieventos.exceptions.usuario;

import co.edu.uniquindio.unieventos.exceptions.RecursoEncontradoException;

public class EmailEncontradoException extends RecursoEncontradoException {

    public EmailEncontradoException(){
        super("Email");
    }
}
