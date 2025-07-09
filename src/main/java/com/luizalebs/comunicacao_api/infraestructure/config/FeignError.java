package com.luizalebs.comunicacao_api.infraestructure.config;

import com.luizalebs.comunicacao_api.infraestructure.exceptions.*;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class FeignError implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {

        String message = errorMessage(response);

                switch (response.status()) {
                    case 404:
                        return new ResourceNotFoundException("Error: " + message);
                    case 400:
                        return new MissingArgumentException("Error: " + message);
                    case 409:
                        return new ConflictException("Error: " + message);
                    case 503:
                        return new EmailException("Error: " + message);
                    default:
                        return new BusinessException("Error: " + message);
                }
    }


    public String errorMessage(Response response) {
        try {
            if (Objects.isNull(response.body())) {
                return "";
            }
            return new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}