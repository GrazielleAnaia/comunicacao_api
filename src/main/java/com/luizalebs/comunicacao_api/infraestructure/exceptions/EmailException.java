package com.luizalebs.comunicacao_api.infraestructure.exceptions;

public class EmailException extends RuntimeException {

    public EmailException(String mensagem) {
        super(mensagem);
    }

    public EmailException() {}


    public EmailException(String mensagem, Throwable throwable) {
        super(mensagem, throwable);
    }
}
