package com.santanna.pontoeletronico.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordCheckoutDto {
    private Long id;
    private String endOfWorkTime;
    private LocalDate endOfWorkDate;
    private String timeWorked;
    private String overtime;
}
