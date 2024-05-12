package com.santanna.pontoeletronico.controller;

import com.santanna.pontoeletronico.domain.dto.*;
import com.santanna.pontoeletronico.service.EmployeeService;
import com.santanna.pontoeletronico.service.TimeRecordingService;
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

    @PostMapping("/entrada")
    public ResponseEntity<RecordCheckinDto> registerCheckIn(@RequestParam String name) {
        EmployeeDto employee = employeeService.getByName(name);
        RecordCheckinDto records = service.registerCheckIn(employee.getName());
        return ResponseEntity.ok(records);
    }

    @PostMapping("/saida")
    public ResponseEntity<RecordCheckoutDto> registerCheckOut(@RequestParam String name) {
        RecordCheckoutDto recordCheckoutDto = service.registerCheckOut(name);
        return ResponseEntity.ok(recordCheckoutDto);
    }


    @GetMapping("/horas-extras")
    public ResponseEntity<OvertimeDto> calculateOvertimeByDateRange(
            @RequestParam String name,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {
        OvertimeDto overtimeDto = service.calculateOvertimeByDateRange(name, dataInicial, dataFinal);
        return ResponseEntity.ok(overtimeDto);
    }

    @GetMapping("/registros")
    public ResponseEntity<List<DetailedTimeRecordingDto>> searchRecordsByDateRange(
            @RequestParam String name,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<DetailedTimeRecordingDto> records = service.searchRecordsByDateRange(name, startDate, endDate);
        return ResponseEntity.ok(records);
    }
}
