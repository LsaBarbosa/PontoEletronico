package com.santanna.pontoeletronico.domain.dto;

import com.santanna.pontoeletronico.domain.entity.RecordWorkTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RecordCheckinDto {
    private Long id;
    private String startOfWorkTime;
    private String startOfWorkDate;


    public RecordCheckinDto(RecordWorkTime dto) {
        this.id = dto.getId();
        this.startOfWorkTime = dto.getStartOfWork().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        this.startOfWorkDate = dto.getStartOfWork().toLocalDate().format(DateTimeFormatter.ISO_DATE);

    }
}
