package com.santanna.pontoeletronico.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailedTimeRecordingDto {
    private Long id;
    private LocalTime endOfWorkTime;
    private LocalDate endOfWorkDate;
    private long timeWorkedInMinutes;
    private long overtime;
}
