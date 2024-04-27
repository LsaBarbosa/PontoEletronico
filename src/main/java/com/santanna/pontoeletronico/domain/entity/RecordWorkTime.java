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
public class RegistroPonto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime entrada;

    private LocalDateTime saida;
    private Long minutosTrabalhadas;

    @Column(name = "data")
    private LocalDate data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @JsonIgnoreProperties("registrosPonto") // Ignora a serialização do campo 'colaborador'
    // ou @JsonIgnore (dependendo do caso)
    private Colaborador colaborador;

}
