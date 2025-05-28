package com.luizalebs.comunicacao_api.infraestructure.exceptions;

public class MissingArgumentException extends RuntimeException{
    public MissingArgumentException(String mensagem) {
        super(mensagem);
    }

    public MissingArgumentException(String mensagem, Throwable throwable) {
        super(mensagem, throwable);
    }
}
