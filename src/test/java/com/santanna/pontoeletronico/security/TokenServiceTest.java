package com.santanna.pontoeletronico.security;

import com.santanna.pontoeletronico.domain.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @Mock
    private Employee employee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Defina o valor do secret através da reflexão
        ReflectionTestUtils.setField(tokenService, "secret", "my-secret-key");
        // Configurar mock do employee
        when(employee.getName()).thenReturn("testEmployee");
    }

    @Test
    void testGenerateTokenSuccess() {
        String token = tokenService.generateToken(employee);
        assertNotNull(token);
    }

    @Test
    void testGenerateTokenFailure() {
        ReflectionTestUtils.setField(tokenService, "secret", ""); // Set an invalid secret to cause JWTCreationException
        assertThrows(RuntimeException.class, () -> tokenService.generateToken(employee));
    }

    @Test
    void testValidateTokenSuccess() {
        String token = tokenService.generateToken(employee);
        String subject = tokenService.validateToken(token);
        assertEquals("testEmployee", subject);
    }

    @Test
    void testValidateTokenFailure() {
        String invalidToken = "invalidToken";
        String subject = tokenService.validateToken(invalidToken);
        assertEquals("", subject);
    }

    @Test
    void testGenExpirationDate() {
        Instant expirationDate = tokenService.genExpirationDate();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expectedExpiration = now.plusHours(2);
        assertEquals(expectedExpiration.toInstant(ZoneOffset.of("-03:00")), expirationDate);
    }
}