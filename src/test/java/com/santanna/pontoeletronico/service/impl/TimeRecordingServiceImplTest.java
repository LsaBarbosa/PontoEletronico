package com.santanna.pontoeletronico.service.impl;

import com.santanna.pontoeletronico.domain.dto.DetailedTimeRecordingDto;
import com.santanna.pontoeletronico.domain.dto.OvertimeDto;
import com.santanna.pontoeletronico.domain.dto.RecordCheckinDto;
import com.santanna.pontoeletronico.domain.dto.RecordCheckoutDto;
import com.santanna.pontoeletronico.domain.entity.Employee;
import com.santanna.pontoeletronico.domain.entity.TimeRecording;
import com.santanna.pontoeletronico.repository.TimeRecordingRepository;
import com.santanna.pontoeletronico.service.exception.DataIntegrityViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TimeRecordingServiceImplTest {


    public static final String EMPLOYEE = "Employee";
    @InjectMocks
    TimeRecordingServiceImpl timeRecordingService;
    @Mock
    TimeRecordingRepository timeRecordingRepository;
    @Mock
    EmployeeServiceImpl employeeService;

    private TimeRecording timeRecording;
    private Employee employee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employee = new Employee();
        employee.setName(EMPLOYEE);

        timeRecording = new TimeRecording();
        timeRecording.setEmployee(employee);
        timeRecording.setStartOfWork(LocalDateTime.now().minusHours(8));
        timeRecording.setEndOfWork(LocalDateTime.now());
    }

    @Test
    void registerCheckin() {
        when(employeeService.getByName(EMPLOYEE)).thenReturn(employee);
        when(timeRecordingRepository.findRegistrationCheckInActive(employee.getName())).thenReturn(null);
        when(timeRecordingRepository.save(any(TimeRecording.class))).thenReturn(timeRecording);

        RecordCheckinDto result = timeRecordingService.registerCheckin(EMPLOYEE);

        assertNotNull(result);
        assertEquals(result.getStartOfWorkDate(), LocalDateTime.now().toLocalDate().toString());
        verify(timeRecordingRepository, times(1)).save(any(TimeRecording.class));
    }

    @Test
    void registerCheckOut() {
        when(employeeService.getByName(EMPLOYEE)).thenReturn(employee);
        when(timeRecordingRepository.findTopByEmployeeAndEndOfWorkIsNullOrderByStartOfWorkDesc(employee))
                .thenReturn(timeRecording);

        RecordCheckoutDto result = timeRecordingService.registerCheckOut(EMPLOYEE);

        assertNotNull(result);
        assertEquals(result.getEndOfWorkDate(), LocalDateTime.now().toLocalDate().toString());
        verify(timeRecordingRepository, times(1)).save(timeRecording);
    }

    @Test
    void registerCheckOutWithoutEntry() {
        when(employeeService.getByName(EMPLOYEE)).thenReturn(employee);
        when(timeRecordingRepository.findTopByEmployeeAndEndOfWorkIsNullOrderByStartOfWorkDesc(employee))
                .thenReturn(null);

        assertThrows(DataIntegrityViolationException.class, () -> timeRecordingService.registerCheckOut(EMPLOYEE));
    }

    @Test
    void calculateOvertimeByDateRange() {
        when(employeeService.getByName(EMPLOYEE)).thenReturn(employee);
        when(timeRecordingRepository.findByEmployeeAndStartOfWorkBetween(any(Employee.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(timeRecording));

        OvertimeDto result = timeRecordingService.calculateOvertimeByDateRange(EMPLOYEE, LocalDate.now().minusDays(1), LocalDate.now());

        assertNotNull(result);
        verify(timeRecordingRepository, times(1)).findByEmployeeAndStartOfWorkBetween(any(Employee.class), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void searchRecordsByDateRange() {
        when(employeeService.getByName(EMPLOYEE)).thenReturn(employee);
        when(timeRecordingRepository.findByEmployeeAndStartOfWorkBetween(any(Employee.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(timeRecording));

        List<DetailedTimeRecordingDto> result = timeRecordingService.searchRecordsByDateRange(EMPLOYEE, LocalDate.now().minusDays(1), LocalDate.now());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(timeRecordingRepository, times(1)).findByEmployeeAndStartOfWorkBetween(any(Employee.class), any(LocalDateTime.class), any(LocalDateTime.class));
    }
}