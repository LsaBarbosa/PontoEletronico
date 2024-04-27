package com.santanna.pontoeletronico.controller;

import com.santanna.pontoeletronico.domain.dto.*;
import com.santanna.pontoeletronico.domain.entity.Employee;
import com.santanna.pontoeletronico.service.TimeRecordingService;

import com.santanna.pontoeletronico.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/ponto")
public class TimeRecordingController {

    @Autowired
    private TimeRecordingService service;

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/entrada/{userId}")
    public ResponseEntity<RecordCheckinDto> registerCheckIn(@PathVariable Long userId) {
        EmployeeDto employee = employeeService.getById(userId);
        RecordCheckinDto records = service.registerCheckIn(employee.getId());
        return ResponseEntity.ok(records);
    }

    @PostMapping("/saida/{userId}")
    public ResponseEntity<RecordCheckoutDto> registerCheckOut(@PathVariable Long userId) {
        RecordCheckoutDto recordCheckoutDto = service.registerCheckOut(userId);
        return ResponseEntity.ok(recordCheckoutDto);
    }


    @GetMapping("/horas-extras/{userId}")
    public ResponseEntity<OvertimeDto> calculateOvertimeByDateRange(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {
        OvertimeDto overtimeDto = service.calculateOvertimeByDateRange(userId, dataInicial, dataFinal);
        return ResponseEntity.ok(overtimeDto);
    }

    @GetMapping("/registros/{userId}")
    public ResponseEntity<List<DetailedTimeRecordingDto>> searchRecordsByDateRange(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<DetailedTimeRecordingDto> records = service.searchRecordsByDateRange(userId, startDate, endDate);
        return ResponseEntity.ok(records);
    }
}
