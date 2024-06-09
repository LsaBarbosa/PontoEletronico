package com.santanna.pontoeletronico.service;

import com.santanna.pontoeletronico.domain.dto.DetailedTimeRecordingDto;
import com.santanna.pontoeletronico.domain.dto.OvertimeDto;
import com.santanna.pontoeletronico.domain.dto.RecordCheckinDto;
import com.santanna.pontoeletronico.domain.dto.RecordCheckoutDto;

import java.time.LocalDate;
import java.util.List;

public interface TimeRecordingService {
    RecordCheckinDto registerCheckin(String name);

    RecordCheckoutDto registerCheckOut(String name);

    OvertimeDto calculateOvertimeByDateRange(String name, LocalDate startDate, LocalDate endDate);

    List<DetailedTimeRecordingDto> searchRecordsByDateRange(String name, LocalDate startDate, LocalDate endDate);

}


