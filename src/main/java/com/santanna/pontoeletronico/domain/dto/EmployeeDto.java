package com.santanna.pontoeletronico.domain.dto;

import com.santanna.pontoeletronico.domain.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeDto {

    private Long id;
    private String name;
  //  private List<RecordCheckinDto> timeRecording;


}
