package com.santanna.pontoeletronico.service.impl;

import com.santanna.pontoeletronico.domain.dto.auth.AuthenticationDTO;
import com.santanna.pontoeletronico.domain.dto.auth.LoginResponseDTO;
import com.santanna.pontoeletronico.domain.entity.Employee;
import com.santanna.pontoeletronico.repository.EmployeeRepository;
import com.santanna.pontoeletronico.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationServiceImpl implements UserDetailsService {

    @Autowired
    private EmployeeRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByName(username);
    }


}
