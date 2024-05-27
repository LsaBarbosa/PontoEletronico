package com.santanna.pontoeletronico.service.impl;

import com.santanna.pontoeletronico.domain.entity.Employee;
import com.santanna.pontoeletronico.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
class AuthorizationServiceImplTest {
    @Mock
    private EmployeeRepository repository;

    @InjectMocks
    private AuthorizationServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testLoadUserByUsername_WhenUserExists_ReturnUserDetails() {
        // Arrange
        String username = "john_doe";
        Employee employee = new Employee();
        employee.setName(username);
        when(repository.findByName(username)).thenReturn(employee);

        // Act
        UserDetails userDetails = authService.loadUserByUsername(username);

        // Assert
        assertEquals(username, userDetails.getUsername());
    }

    @Test
    void testLoadUserByUsername_WhenUserDoesNotExist_ThrowUsernameNotFoundException() {
        // Arrange
        String username = "non_existing_user";
        when(repository.findByName(username)).thenThrow(new UsernameNotFoundException("Username not found"));
        try {
            authService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            assertThrows(UsernameNotFoundException.class, () -> authService.loadUserByUsername(username));
        }
    }
}