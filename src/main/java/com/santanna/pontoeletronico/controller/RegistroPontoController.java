package com.santanna.pontoeletronico.controller;

import com.santanna.pontoeletronico.domain.dto.RegistroPontoDetalhadoDto;
import com.santanna.pontoeletronico.domain.dto.RegistroPontoDto;
import com.santanna.pontoeletronico.domain.dto.RegistroPontoUsuarioDto;
import com.santanna.pontoeletronico.domain.entity.Usuario;
import com.santanna.pontoeletronico.service.RegistroPontoService;

import com.santanna.pontoeletronico.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@RestController
@RequestMapping("/registro-ponto")
public class RegistroPontoController {

    @Autowired
    private RegistroPontoService service;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/entrada/{userId}")
    public ResponseEntity<RegistroPontoDto> registrarEntrada(@PathVariable Long userId) {
        Usuario usuario = usuarioService.getById(userId);
        RegistroPontoDto registro = service.registrarEntrada(usuario.getId());
        return ResponseEntity.ok(registro);
    }

    @PostMapping("/saida/{userId}")
    public ResponseEntity<RegistroPontoDetalhadoDto> registrarSaida(@PathVariable Long userId) {
        RegistroPontoDetalhadoDto registroPontoDetalhadoDto = service.registrarSaida(userId);
        return ResponseEntity.ok(registroPontoDetalhadoDto);
    }

    @GetMapping("/{userId}/registros-ponto-detalhados")
    public ResponseEntity<RegistroPontoDetalhadoDto> buscarRegistrosPontoDetalhados(@PathVariable Long userId) {
        RegistroPontoDetalhadoDto registroPontoDetalhadoDto = service.registrarSaida(userId);
        return ResponseEntity.ok(registroPontoDetalhadoDto);
    }
    @GetMapping("/{userId}/horas-extras")
    public ResponseEntity<Long> calcularHorasExtrasPorIntervaloDeDatas(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {
        long totalHorasExtras = service.calcularHorasExtrasPorIntervaloDeDatas(userId, dataInicial, dataFinal);
        return ResponseEntity.ok(totalHorasExtras);
    }

    @GetMapping("/registros-ponto/{userId}")
    public ResponseEntity<RegistroPontoUsuarioDto> buscarRegistrosPontoPorUsuario(@PathVariable Long userId) {
        RegistroPontoUsuarioDto registrosPontoUsuarioDto = service.buscarRegistrosPontoPorUsuario(userId);
        return ResponseEntity.ok(registrosPontoUsuarioDto);
    }

}
