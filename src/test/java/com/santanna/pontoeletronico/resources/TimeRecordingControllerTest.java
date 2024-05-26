package com.santanna.pontoeletronico.resources;

import com.santanna.pontoeletronico.domain.dto.DetailedTimeRecordingDto;
import com.santanna.pontoeletronico.domain.dto.OvertimeDto;
import com.santanna.pontoeletronico.domain.dto.RecordCheckinDto;
import com.santanna.pontoeletronico.domain.dto.RecordCheckoutDto;
import com.santanna.pontoeletronico.domain.entity.Employee;
import com.santanna.pontoeletronico.service.EmployeeService;
import com.santanna.pontoeletronico.service.TimeRecordingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class TimeRecordingControllerTest {
    public static final String EIGHT_HOURS = "08:00";
    public static final String DATE = "2024-05-25";
    public static final String SEVENTEEN_HOURS = "17:00";
    public static final String ZERO = "00:00";
    public static final String TEN_HOURS = "10:00";
    public static final String EMPLOYEE = "John Doe";
    @InjectMocks
    private TimeRecordingController timeRecordingController;

    @Mock
    private TimeRecordingService timeService;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private ModelMapper mapper;

    private Employee employee;
    private RecordCheckinDto recordCheckinDto;
    private RecordCheckoutDto recordCheckoutDto;
    private OvertimeDto overtimeDto;
    private DetailedTimeRecordingDto detailedTimeRecordingDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        employee = new Employee();
        employee.setName(EMPLOYEE);

        recordCheckinDto = new RecordCheckinDto();
        recordCheckinDto.setId(1L);
        recordCheckinDto.setStartOfWorkTime(EIGHT_HOURS);
        recordCheckinDto.setStartOfWorkDate(DATE);

        recordCheckoutDto = new RecordCheckoutDto();
        recordCheckoutDto.setId(1L);
        recordCheckoutDto.setEndOfWorkTime(SEVENTEEN_HOURS);
        recordCheckoutDto.setEndOfWorkDate(DATE);
        recordCheckoutDto.setTimeWorked(EIGHT_HOURS);
        recordCheckoutDto.setOvertime(ZERO);

        overtimeDto = new OvertimeDto();
        overtimeDto.setTotalOvertime(TEN_HOURS);

        detailedTimeRecordingDto = new DetailedTimeRecordingDto();
        detailedTimeRecordingDto.setId(1L);
        detailedTimeRecordingDto.setStartOfWork(EIGHT_HOURS);
        detailedTimeRecordingDto.setEndOfWork(SEVENTEEN_HOURS);
        detailedTimeRecordingDto.setStartDate(DATE);
        detailedTimeRecordingDto.setEndDate(DATE);
        detailedTimeRecordingDto.setTimeWorked(EIGHT_HOURS);
        detailedTimeRecordingDto.setOvertime(ZERO);
    }

    @Test
    void whenRegisterCheckInThenReturnSuccess() {
        when(employeeService.getByName(anyString())).thenReturn(employee);
        when(timeService.registerCheckin(anyString())).thenReturn(recordCheckinDto);
        when(mapper.map(any(), any())).thenReturn(recordCheckinDto);

        ResponseEntity<RecordCheckinDto> response = timeRecordingController.registerCheckIn(EMPLOYEE);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(RecordCheckinDto.class, response.getBody().getClass());
        assertEquals(1L, response.getBody().getId());
        assertEquals(EIGHT_HOURS, response.getBody().getStartOfWorkTime());
        assertEquals(DATE, response.getBody().getStartOfWorkDate());
    }

    @Test
    void whenRegisterCheckOutThenReturnSuccess() {
        when(employeeService.getByName(anyString())).thenReturn(employee);
        when(timeService.registerCheckOut(anyString())).thenReturn(recordCheckoutDto);
        when(mapper.map(any(), any())).thenReturn(recordCheckoutDto);

        ResponseEntity<RecordCheckoutDto> response = timeRecordingController.registerCheckOut(EMPLOYEE);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(RecordCheckoutDto.class, response.getBody().getClass());
        assertEquals(1L, response.getBody().getId());
        assertEquals(SEVENTEEN_HOURS, response.getBody().getEndOfWorkTime());
        assertEquals(DATE, response.getBody().getEndOfWorkDate());
        assertEquals(EIGHT_HOURS, response.getBody().getTimeWorked());
        assertEquals(ZERO, response.getBody().getOvertime());
    }

    @Test
    void whenCalculateOvertimeByDateRangeThenReturnSuccess() {
        when(timeService.calculateOvertimeByDateRange(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(overtimeDto);
        when(mapper.map(any(), any())).thenReturn(overtimeDto);

        ResponseEntity<OvertimeDto> response = timeRecordingController.calculateOvertimeByDateRange(
                EMPLOYEE, LocalDate.of(2024, 5, 1), LocalDate.of(2024, 5, 31));

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(OvertimeDto.class, response.getBody().getClass());
        assertEquals(TEN_HOURS, response.getBody().getTotalOvertime());
    }

    @Test
    void whenSearchRecordsByDateRangeThenReturnSuccess() {
        List<DetailedTimeRecordingDto> records = List.of(detailedTimeRecordingDto);
        when(timeService.searchRecordsByDateRange(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(records);

        ResponseEntity<List<DetailedTimeRecordingDto>> response = timeRecordingController.searchRecordsByDateRange(
                EMPLOYEE, LocalDate.of(2024, 5, 1), LocalDate.of(2024, 5, 31));

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertEquals(DetailedTimeRecordingDto.class, response.getBody().get(0).getClass());
        assertEquals(1L, response.getBody().get(0).getId());
        assertEquals(EIGHT_HOURS, response.getBody().get(0).getStartOfWork());
        assertEquals(SEVENTEEN_HOURS, response.getBody().get(0).getEndOfWork());
        assertEquals(DATE, response.getBody().get(0).getStartDate());
        assertEquals(DATE, response.getBody().get(0).getEndDate());
        assertEquals(EIGHT_HOURS, response.getBody().get(0).getTimeWorked());
        assertEquals(ZERO, response.getBody().get(0).getOvertime());
    }
}