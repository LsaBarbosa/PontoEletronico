package com.santanna.pontoeletronico.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordCheckoutDto {
    private Long id;
    private String endOfWorkTime;
    private String endOfWorkDate;
    private String timeWorked;
    private String overtime;
}
