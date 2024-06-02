package com.santanna.pontoeletronico.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TimeRecording {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private ZonedDateTime startOfWork;
    private ZonedDateTime endOfWork;
    private Long timeWorkedInMinutes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("RecordWorkTime")
    private Employee employee;

    @PrePersist
    public void prePersist() {
        this.startOfWork = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));
    }
}
