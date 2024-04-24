package com.santanna.pontoeletronico.controller;

import com.santanna.pontoeletronico.domain.dto.RegistroDto;
import com.santanna.pontoeletronico.domain.dto.UsuarioRegisterDto;
import com.santanna.pontoeletronico.domain.entity.Usuario;
import com.santanna.pontoeletronico.service.UsuarioService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioRegisterDto> getById(@PathVariable Long id) {
        Usuario usuario = usuarioService.getById(id);
        UsuarioRegisterDto usuarioDto = new UsuarioRegisterDto();
        usuarioDto.setId(usuario.getId());
        usuarioDto.setName(usuario.getName());
        List<RegistroDto> registroDto = usuario.getRegistrosPonto().stream()
                .map(registro -> new RegistroDto(registro.getId(), registro.getEntrada(), registro.getSaida(),registro.getMinutosTrabalhadas()))
                .collect(Collectors.toList());
        usuarioDto.setRegistrosPonto(registroDto);
        return ResponseEntity.ok(usuarioDto);
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> getAll() {
        List<Usuario> usuarios = usuarioService.getAll();
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping
    public ResponseEntity<Usuario> create(@RequestBody Usuario usuario) {
        Usuario createdUsuario = usuarioService.create(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUsuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> update(@PathVariable Long id, @RequestBody Usuario usuario) {
        Usuario updatedUsuario = usuarioService.update(id, usuario);
        return ResponseEntity.ok(updatedUsuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
