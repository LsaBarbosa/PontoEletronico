package com.santanna.pontoeletronico.resources.exceptions;

import com.santanna.pontoeletronico.service.exception.DataIntegrityViolationException;
import com.santanna.pontoeletronico.service.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ResourceExceptionHandlerTest {

    public static final String EMPLOYEE_NOT_FOUND = "Colaborador não encontrado";
    public static final String EMPLOYEE_ALREADY_EXIST = "Já existe um colaborador com este nome.";

    @InjectMocks
    private ResourceExceptionHandler ecxeptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void whenObjectNotFoundExceptionThenReturnAResponseEntity() {
        ResponseEntity<StandardError> response = ecxeptionHandler
                .objectNotFound(
                        new ObjectNotFoundException(EMPLOYEE_NOT_FOUND),
                        new MockHttpServletRequest());

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(StandardError.class, response.getBody().getClass());
        assertEquals(EMPLOYEE_NOT_FOUND, response.getBody().getError());
        assertEquals(404, response.getBody().getStatus());
        assertNotEquals("/user/2", response.getBody().getPath());
        assertNotEquals(LocalDateTime.now(), response.getBody().getTimestamp());
    }

    @Test
    void dataIntegrityViolationException() {
        ResponseEntity<StandardError> response = ecxeptionHandler
                .dataIntegrityViolation(
                        new DataIntegrityViolationException(EMPLOYEE_ALREADY_EXIST),
                        new MockHttpServletRequest());

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(StandardError.class, response.getBody().getClass());
        assertEquals(EMPLOYEE_ALREADY_EXIST, response.getBody().getError());
        assertEquals(400, response.getBody().getStatus());
    }
}