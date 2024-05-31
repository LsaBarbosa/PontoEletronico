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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
class AuthorizationServiceImplTest {
    public static final String USERNAME_NOT_FOUND = "Username not found";
    @Mock
    private EmployeeRepository repository;

    @InjectMocks
    private AuthorizationServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_WhenUserExists_ReturnUserDetails() {

        String username = "john_doe";
        Employee employee = new Employee();
        employee.setName(username);
        when(repository.findByName(username)).thenReturn(employee);


        UserDetails userDetails = authService.loadUserByUsername(username);


        assertEquals(username, userDetails.getUsername());
    }

    @Test
    void testLoadUserByUsername_WhenUserDoesNotExist_ThrowUsernameNotFoundException() {

        String username = "non_existing_user";
        when(repository.findByName(username)).thenThrow(new UsernameNotFoundException(USERNAME_NOT_FOUND));
        try {
            authService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            assertThrows(UsernameNotFoundException.class, () -> authService.loadUserByUsername(username));
        }
    }
}