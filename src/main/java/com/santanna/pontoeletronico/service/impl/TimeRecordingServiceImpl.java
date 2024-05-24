package com.santanna.pontoeletronico.service.impl;

import com.santanna.pontoeletronico.domain.dto.DetailedTimeRecordingDto;
import com.santanna.pontoeletronico.domain.dto.OvertimeDto;
import com.santanna.pontoeletronico.domain.dto.RecordCheckinDto;
import com.santanna.pontoeletronico.domain.dto.RecordCheckoutDto;
import com.santanna.pontoeletronico.domain.entity.Employee;
import com.santanna.pontoeletronico.domain.entity.RecordWorkTime;
import com.santanna.pontoeletronico.service.EmployeeService;
import com.santanna.pontoeletronico.service.TimeRecordingService;
import com.santanna.pontoeletronico.service.exception.DataIntegrityViolationException;
import com.santanna.pontoeletronico.service.exception.ObjectNotFoundException;
import com.santanna.pontoeletronico.utils.TimeFormattingUtils;
import com.santanna.pontoeletronico.repository.EmployeeRepository;
import com.santanna.pontoeletronico.repository.TimeRecordingRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class TimeRecordingServiceImpl implements TimeRecordingService {


    public static final String SEM_REGISTRO_DE_ENTRADA = "Não há registro de entrada para o usuário";
    public static final String REGISTRO_DE_SAIDA = "Registro de Saída não registrado, encerre o registro de entrada aberto";
    @Autowired
    private TimeRecordingRepository repository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private EmployeeService employeeService;


    @Override
    public RecordCheckinDto registerCheckin(String name) {
        var employee = employeeService.getByName(name);
        var activeCheckin = repository.findRegistrationCheckInActive(employee.getName());

        if (activeCheckin != null && activeCheckin.getEndOfWork() == null) {
        throw new DataIntegrityViolationException(REGISTRO_DE_SAIDA);
        }

        var newRegister = new RecordWorkTime();
        newRegister.setEmployee(employee);
        newRegister.setStartOfWork(LocalDateTime.now());
        var save = repository.save(newRegister);
        return new RecordCheckinDto(save);
    }

    @Override
    public RecordCheckoutDto registerCheckOut(String name) {
        var employee = employeeService.getByName(name);

        RecordWorkTime recordCheckIn = repository.findTopByEmployeeAndEndOfWorkIsNullOrderByStartOfWorkDesc(employee);

        if (recordCheckIn == null) {
            throw new DataIntegrityViolationException(SEM_REGISTRO_DE_ENTRADA);
        }

        LocalDateTime timeCheckOut = LocalDateTime.now();
        recordCheckIn.setEndOfWork(timeCheckOut);
        repository.save(recordCheckIn);

        return TimeFormattingUtils.formatRecordCheckoutDto(recordCheckIn);
    }

    @Override
    public OvertimeDto calculateOvertimeByDateRange(String name, LocalDate startDate, LocalDate endDate) {
        var employee = employeeService.getByName(name);

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay(); // Adiciona um dia e pega o começo do dia seguinte

        List<RecordWorkTime> records = repository.findByEmployeeAndStartOfWorkBetween(employee, startDateTime, endDateTime);

        return TimeFormattingUtils.calculateOvertime(records);
    }


    @Override
    public List<DetailedTimeRecordingDto> searchRecordsByDateRange(String name, LocalDate startDate, LocalDate endDate) {
        var employee = employeeService.getByName(name);

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay(); // Adiciona um dia e pega o começo do dia seguinte

        List<RecordWorkTime> records = repository.findByEmployeeAndStartOfWorkBetween(employee, startDateTime, endDateTime);

        return records.stream()
                .map(TimeFormattingUtils::toDetailedTimeRecordingDto)
                .collect(Collectors.toList());
    }

}


