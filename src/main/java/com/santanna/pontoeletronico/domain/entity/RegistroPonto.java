package com.santanna.pontoeletronico.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.santanna.pontoeletronico.domain.dto.RegistroPontoDto;
import jakarta.persistence.*;
import lombok.*;


import java.time.Duration;
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
    @JsonIgnoreProperties("registrosPonto") // Ignora a serialização do campo 'usuario'
    // ou @JsonIgnore (dependendo do caso)
    private  Usuario usuario;

}
