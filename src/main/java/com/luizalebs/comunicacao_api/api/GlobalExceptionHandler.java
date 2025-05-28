package com.luizalebs.comunicacao_api.api;


import com.luizalebs.comunicacao_api.infraestructure.exceptions.ConflictException;
import com.luizalebs.comunicacao_api.infraestructure.exceptions.EmailException;
import com.luizalebs.comunicacao_api.infraestructure.exceptions.MissingArgumentException;
import com.luizalebs.comunicacao_api.infraestructure.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {


@ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException e) {
    return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);

}

@ExceptionHandler(MissingArgumentException.class)
    public ResponseEntity<String> handleMissingArgumentException(MissingArgumentException e) {
    return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
}

@ExceptionHandler(EmailException.class)
    public ResponseEntity<String> handleEmailException(EmailException e) {
    return new ResponseEntity(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
}
@ExceptionHandler(ConflictException.class)
    public ResponseEntity<String> handleConflictException(ConflictException e) {
    return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
}

}
