package com.santanna.pontoeletronico.resources;

import com.santanna.pontoeletronico.domain.dto.DetailedTimeRecordingDto;
import com.santanna.pontoeletronico.domain.dto.OvertimeDto;
import com.santanna.pontoeletronico.domain.dto.RecordCheckinDto;
import com.santanna.pontoeletronico.domain.dto.RecordCheckoutDto;
import com.santanna.pontoeletronico.domain.entity.Employee;
import com.santanna.pontoeletronico.service.EmployeeService;
import com.santanna.pontoeletronico.service.TimeRecordingService;
import org.modelmapper.ModelMapper;
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
    private TimeRecordingService timeService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ModelMapper mapper;

    @PostMapping("/entrada")
    public ResponseEntity<RecordCheckinDto> registerCheckIn(@RequestParam String name) {
        Employee employee = employeeService.getByName(name);
        RecordCheckinDto records = timeService.registerCheckin(employee.getName());
        return ResponseEntity.ok().body(mapper.map(records, RecordCheckinDto.class));
    }

    @PostMapping("/saida")
    public ResponseEntity<RecordCheckoutDto> registerCheckOut(@RequestParam String name) {
        RecordCheckoutDto recordCheckoutDto = timeService.registerCheckOut(name);
        return ResponseEntity.ok().body(mapper.map(recordCheckoutDto,RecordCheckoutDto.class));
    }


    @GetMapping("/horas-extras")
    public ResponseEntity<OvertimeDto> calculateOvertimeByDateRange(
            @RequestParam String name,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {
        OvertimeDto overtimeDto = timeService.calculateOvertimeByDateRange(name, dataInicial, dataFinal);
        return ResponseEntity.ok(mapper.map(overtimeDto,OvertimeDto.class));
    }

    @GetMapping("/registros")
    public ResponseEntity<List<DetailedTimeRecordingDto>> searchRecordsByDateRange(
            @RequestParam String name,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<DetailedTimeRecordingDto> records = timeService.searchRecordsByDateRange(name, startDate, endDate);
        return ResponseEntity.ok().body(records);
    }
}
