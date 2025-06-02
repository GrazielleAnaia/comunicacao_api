package com.luizalebs.comunicacao_api.infrastructure.exceptions;


import com.luizalebs.comunicacao_api.infraestructure.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ResourceNotFoundExceptionTest {


//    @Test
//    void testExceptionCause() {
//        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
//                () -> {throw new ResourceNotFoundException("Wrapper", new IllegalArgumentException("Cause"));});
//
//        Throwable cause = exception.getCause();
//        assertNotNull(cause);
//        assertInstanceOf(IllegalArgumentException.class, cause);
//        assertEquals("Cause", cause.getMessage());
//    }
}
