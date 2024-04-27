package com.santanna.pontoeletronico.domain.dto;

import com.santanna.pontoeletronico.domain.entity.RecordWorkTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RecordDto {
    private Long id;
    private LocalDateTime startOfWork;


    public RecordDto(RecordWorkTime dto) {
        this.id = dto.getId();
        this.startOfWork = dto.getStartOfWork();

    }
}
