package com.santanna.pontoeletronico.service;

import com.santanna.pontoeletronico.domain.dto.*;

import com.santanna.pontoeletronico.domain.entity.Employee;
import com.santanna.pontoeletronico.domain.entity.RecordWorkTime;
import com.santanna.pontoeletronico.repository.TimeRecordingRepository;

import com.santanna.pontoeletronico.repository.EmployeeRepository;
import com.santanna.pontoeletronico.utils.TimeFormattingUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.Duration;
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


    public RecordCheckinDto registerCheckIn(Long userId) {
        RecordWorkTime activeCheckin = repository.findRegistrationCheckInActive(userId);

        if (activeCheckin != null && activeCheckin.getEndOfWork() == null) {
            // Já existe um registro de entrada ativo, atualiza a hora de saída
            activeCheckin.setEndOfWork(LocalDateTime.now());
            repository.save(activeCheckin);
        }

        // Cria um novo registro de entrada
        RecordWorkTime newRecord = new RecordWorkTime();
        // Obtém o usuário pelo ID
        Employee employee = employeeRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        newRecord.setEmployee(employee);
        newRecord.setStartOfWork(LocalDateTime.now());
        repository.save(newRecord);

        return new RecordCheckinDto(newRecord);
    }

    public RecordCheckoutDto registerCheckOut(Long userId) {
        Employee employee = employeeRepository.findById(userId)
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


    public OvertimeDto calculateOvertimeByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        Employee employee = employeeRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay(); // Adiciona um dia e pega o começo do dia seguinte

        List<RecordWorkTime> records = repository.findByEmployeeAndStartOfWorkBetween(employee, startDateTime, endDateTime);

        return TimeFormattingUtils.calculateOvertime(records);
    }



    public List<DetailedTimeRecordingDto> searchRecordsByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        Employee employee = employeeRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay(); // Adiciona um dia e pega o começo do dia seguinte

        List<RecordWorkTime> records = repository.findByEmployeeAndStartOfWorkBetween(employee, startDateTime, endDateTime);

        return records.stream()
                .map(TimeFormattingUtils::toDetailedTimeRecordingDto)
                .collect(Collectors.toList());
    }

}


