package com.santanna.pontoeletronico.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class DetailedTimeRecordingDto {

    private Long id;
    private String startOfWork;
    private String endOfWork;
    private LocalDate startDate;
    private LocalDate endDate;
    private String timeWorked;
    private String overtime;


}
