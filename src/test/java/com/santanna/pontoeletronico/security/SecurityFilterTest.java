package com.santanna.pontoeletronico.security;

import com.santanna.pontoeletronico.repository.EmployeeRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class SecurityFilterTest {

    @InjectMocks
    private SecurityFilter securityFilter;

    @Mock
    private TokenService tokenService;

    @Mock
    private EmployeeRepository repository;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    public void setUp() {
        AutoCloseable autoCloseable = MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @Test
    public void doFilterInternal_withValidToken_shouldAuthenticateUser() throws IOException, ServletException {
        // Arrange
        String token = "validToken";
        String username = "validUser";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();

        UserDetails userDetails = User.builder()
                .username(username)
                .password("password")
                .authorities("ROLE_USER")
                .build();

        when(tokenService.validateToken(token)).thenReturn(username);
        when(repository.findByName(username)).thenReturn(userDetails);

        // Act
        securityFilter.doFilterInternal(request, response, filterChain);

        // Assert
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertNotNull(authentication.getPrincipal());
        assertEquals(username, ((UserDetails) authentication.getPrincipal()).getUsername());

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    public void doFilterInternal_withInvalidToken_shouldNotAuthenticateUser() throws ServletException, IOException {
        // Arrange
        String token = "invalidToken";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(tokenService.validateToken(token)).thenReturn(null);

        // Act
        securityFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    public void doFilterInternal_withNoToken_shouldNotAuthenticateUser() throws ServletException, IOException {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Act
        securityFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        verify(filterChain, times(1)).doFilter(request, response);
    }

}