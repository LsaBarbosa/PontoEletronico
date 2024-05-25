package com.santanna.pontoeletronico.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("RecordWorkTime")
    private Employee employee;

}
