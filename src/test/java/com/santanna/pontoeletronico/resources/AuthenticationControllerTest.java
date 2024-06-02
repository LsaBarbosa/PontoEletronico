package com.santanna.pontoeletronico.resources;

import com.santanna.pontoeletronico.domain.dto.auth.AuthenticationDTO;
import com.santanna.pontoeletronico.domain.dto.auth.LoginResponseDTO;
import com.santanna.pontoeletronico.domain.dto.auth.RegisterDTO;
import com.santanna.pontoeletronico.domain.role.EmployeeRole;
import com.santanna.pontoeletronico.service.impl.Auth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthenticationControllerTest {
    public static final String TOKEN = "some-token";
    public static final String name = "test@example.com";
    public static final String PASSWORD = "password";

    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private Auth auth;

    private AuthenticationDTO authenticationDTO;
    private LoginResponseDTO loginResponseDTO;
    private RegisterDTO registerDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startAuthentication();
    }

    @Test
    void whenLoginThenReturnSuccess() {
        when(auth.login(any(AuthenticationDTO.class))).thenReturn(loginResponseDTO);

        ResponseEntity<LoginResponseDTO> response = authenticationController.login(authenticationDTO);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(LoginResponseDTO.class, response.getBody().getClass());
        assertEquals(TOKEN, response.getBody().token());
    }

    @Test
    void whenRegisterThenReturnSuccess() {
        when(auth.register(any(RegisterDTO.class))).thenReturn(registerDTO);

        ResponseEntity<RegisterDTO> response = authenticationController.register(registerDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private void startAuthentication() {
        authenticationDTO = new AuthenticationDTO(name, PASSWORD);
        loginResponseDTO = new LoginResponseDTO(TOKEN);
        registerDTO = new RegisterDTO(name, PASSWORD, EmployeeRole.ADMIN);
    }
}