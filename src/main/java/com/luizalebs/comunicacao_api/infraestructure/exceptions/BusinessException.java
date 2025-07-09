package com.luizalebs.comunicacao_api.infraestructure.exceptions;

public class BusinessException extends RuntimeException {
    private static final long servialVersionUID = 1L;
    public BusinessException(String mensagem) {
        super(mensagem);
    }

    public BusinessException(String mensagem, Throwable throwable) {
        super(mensagem, throwable);
    }
}
