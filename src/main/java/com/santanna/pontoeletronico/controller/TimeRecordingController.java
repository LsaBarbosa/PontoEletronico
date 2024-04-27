package com.santanna.pontoeletronico.controller;

import com.santanna.pontoeletronico.domain.dto.RecordDto;
import com.santanna.pontoeletronico.domain.dto.DetailedTimeRecordingDto;
import com.santanna.pontoeletronico.domain.dto.EmployeeTimeRecordDto;
import com.santanna.pontoeletronico.domain.entity.Employee;
import com.santanna.pontoeletronico.service.TimeRecordingService;

import com.santanna.pontoeletronico.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@RestController
@RequestMapping("/registro-ponto")
public class RegistroPontoController {

    @Autowired
    private TimeRecordingService service;

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/entrada/{userId}")
    public ResponseEntity<RecordDto> registrarEntrada(@PathVariable Long userId) {
        Employee employee = employeeService.getById(userId);
        RecordDto registro = service.registrarEntrada(employee.getId());
        return ResponseEntity.ok(registro);
    }

    @PostMapping("/saida/{userId}")
    public ResponseEntity<DetailedTimeRecordingDto> registrarSaida(@PathVariable Long userId) {
        DetailedTimeRecordingDto detailedTimeRecordingDto = service.registrarSaida(userId);
        return ResponseEntity.ok(detailedTimeRecordingDto);
    }

    @GetMapping("/{userId}/registros-ponto-detalhados")
    public ResponseEntity<DetailedTimeRecordingDto> buscarRegistrosPontoDetalhados(@PathVariable Long userId) {
        DetailedTimeRecordingDto detailedTimeRecordingDto = service.registrarSaida(userId);
        return ResponseEntity.ok(detailedTimeRecordingDto);
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
    public ResponseEntity<EmployeeTimeRecordDto> buscarRegistrosPontoPorUsuario(@PathVariable Long userId) {
        EmployeeTimeRecordDto registrosPontoUsuarioDto = service.buscarRegistrosPontoPorUsuario(userId);
        return ResponseEntity.ok(registrosPontoUsuarioDto);
    }

}
