package com.santanna.pontoeletronico.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class DetailedTimeRecordingDto {

    private Long id;
    private String startOfWork;
    private String endOfWork;
    private String startDate;
    private String endDate;
    private String timeWorked;
    private String overtime;
}
