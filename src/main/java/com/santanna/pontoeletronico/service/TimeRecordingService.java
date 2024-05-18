package com.santanna.pontoeletronico.service;

import com.santanna.pontoeletronico.domain.dto.DetailedTimeRecordingDto;
import com.santanna.pontoeletronico.domain.dto.OvertimeDto;
import com.santanna.pontoeletronico.domain.dto.RecordCheckinDto;
import com.santanna.pontoeletronico.domain.dto.RecordCheckoutDto;
import com.santanna.pontoeletronico.domain.entity.Employee;
import com.santanna.pontoeletronico.domain.entity.RecordWorkTime;
import com.santanna.pontoeletronico.repository.EmployeeRepository;
import com.santanna.pontoeletronico.repository.TimeRecordingRepository;
import com.santanna.pontoeletronico.utils.TimeFormattingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class TimeRecordingService {

    @Autowired
    private TimeRecordingRepository repository;
    @Autowired
    private EmployeeRepository employeeRepository;


    public RecordCheckinDto registerCheckIn(String name) {
        RecordWorkTime activeCheckin = repository.findRegistrationCheckInActive(name);

        if (activeCheckin != null && activeCheckin.getEndOfWork() == null) {

            activeCheckin.setEndOfWork(LocalDateTime.now());
            repository.save(activeCheckin);
        }


        RecordWorkTime newRecord = new RecordWorkTime();

        Employee employee = employeeRepository.findByName(name).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        newRecord.setEmployee(employee);
        newRecord.setStartOfWork(LocalDateTime.now());
        repository.save(newRecord);

        return new RecordCheckinDto(newRecord);
    }

    public RecordCheckoutDto registerCheckOut(String name) {
        Employee employee = employeeRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        RecordWorkTime recordCheckIn = repository.findTopByEmployeeAndEndOfWorkIsNullOrderByStartOfWorkDesc(employee);

        if (recordCheckIn == null) {
            throw new RuntimeException("Não há registro de entrada para o usuário.");
        }

        LocalDateTime timeCheckOut = LocalDateTime.now();
        recordCheckIn.setEndOfWork(timeCheckOut);
        repository.save(recordCheckIn);

        return TimeFormattingUtils.formatRecordCheckoutDto(recordCheckIn);
    }


    public OvertimeDto calculateOvertimeByDateRange(String name, LocalDate startDate, LocalDate endDate) {
        Employee employee = employeeRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay(); // Adiciona um dia e pega o começo do dia seguinte

        List<RecordWorkTime> records = repository.findByEmployeeAndStartOfWorkBetween(employee, startDateTime, endDateTime);

        return TimeFormattingUtils.calculateOvertime(records);
    }



    public List<DetailedTimeRecordingDto> searchRecordsByDateRange( String name, LocalDate startDate, LocalDate endDate) {
        Employee employee = employeeRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay(); // Adiciona um dia e pega o começo do dia seguinte

        List<RecordWorkTime> records = repository.findByEmployeeAndStartOfWorkBetween(employee, startDateTime, endDateTime);

        return records.stream()
                .map(TimeFormattingUtils::toDetailedTimeRecordingDto)
                .collect(Collectors.toList());
    }

}


