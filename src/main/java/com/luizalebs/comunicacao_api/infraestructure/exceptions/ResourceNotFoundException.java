package com.luizalebs.comunicacao_api.infraestructure.exceptions;


import jakarta.transaction.Transactional;
import lombok.ToString;


public class ResourceNotFoundException extends RuntimeException {
    private static final long servialVersionUID = 1L;

    public ResourceNotFoundException(String mensagem) {
        super(mensagem);
    }

    public ResourceNotFoundException(String mensagem, Throwable throwable) {
        super(mensagem, throwable);
    }

}
