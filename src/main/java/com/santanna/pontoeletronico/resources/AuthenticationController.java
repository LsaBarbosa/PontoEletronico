package com.santanna.pontoeletronico.resources;

import com.santanna.pontoeletronico.domain.dto.auth.AuthenticationDTO;
import com.santanna.pontoeletronico.domain.dto.auth.LoginResponseDTO;
import com.santanna.pontoeletronico.domain.dto.auth.RegisterDTO;
import com.santanna.pontoeletronico.domain.entity.Employee;
import com.santanna.pontoeletronico.repository.EmployeeRepository;
import com.santanna.pontoeletronico.security.TokenService;
import com.santanna.pontoeletronico.service.Auth;
import com.santanna.pontoeletronico.service.impl.AuthorizationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {

    @Autowired
    private Auth auth;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody AuthenticationDTO data) {
        var token = auth.login(data);
        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterDTO> register(@RequestBody RegisterDTO data) {
       auth.register(data);
        return ResponseEntity.ok().build();
    }

}
