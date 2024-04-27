package com.santanna.pontoeletronico.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RecordWorkTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startOfWork;

    private LocalDateTime endOfWork;
    private Long timeWorkedInMinutes;

    //@Column(name = "data")
   // private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    @JsonIgnoreProperties("RecordWorkTime") // Ignora a serialização do campo 'colaborador'
    private Employee employee;

}
