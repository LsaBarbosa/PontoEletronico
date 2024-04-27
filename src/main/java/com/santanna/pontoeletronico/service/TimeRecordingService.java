package com.santanna.pontoeletronico.service;

import com.santanna.pontoeletronico.domain.dto.RecordDto;
import com.santanna.pontoeletronico.domain.dto.DetailedTimeRecordingDto;

import com.santanna.pontoeletronico.domain.dto.EmployeeTimeRecordDto;
import com.santanna.pontoeletronico.domain.entity.Employee;
import com.santanna.pontoeletronico.domain.entity.RecordWorkTime;
import com.santanna.pontoeletronico.repository.RegistroPontoRepository;

import com.santanna.pontoeletronico.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class RegistroPontoService {

    @Autowired
    private RegistroPontoRepository repository;
    @Autowired
    private UsuarioRepository usuarioRepository;


    public RecordDto registrarEntrada(Long userId) {
        RecordWorkTime registroAtivo = repository.findRegistroEntradaAtivo(userId);

        if (registroAtivo != null && registroAtivo.getEndOfWork() == null) {
            // Já existe um registro de entrada ativo, atualiza a hora de saída
            registroAtivo.setEndOfWork(LocalDateTime.now());
            repository.save(registroAtivo);
        }

        // Cria um novo registro de entrada
        RecordWorkTime novoRegistro = new RecordWorkTime();
        // Obtém o usuário pelo ID
        Employee employee = usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        novoRegistro.setEmployee(employee);
        novoRegistro.setStartOfWork(LocalDateTime.now());
        repository.save(novoRegistro);

        return new RecordDto(novoRegistro);
    }



    public DetailedTimeRecordingDto registrarSaida(Long userId) {
        Employee employee = usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Encontra o último registro de ponto de entrada para o usuário
        RecordWorkTime registroEntrada = repository.findTopByColaboradorAndEndOfWorkIsNullOrderByStartOfWorkDesc(employee);

        if (registroEntrada == null) {
            throw new RuntimeException("Não há registro de entrada para o usuário.");
        }

        // Registra a saída e calcula as horas trabalhadas e as horas extras
        LocalDateTime horaSaida = LocalDateTime.now();
        registroEntrada.setEndOfWork(horaSaida);
        repository.save(registroEntrada);

        Duration duracaoTrabalho = Duration.between(registroEntrada.getStartOfWork(), horaSaida);
        long minutosTrabalhados = duracaoTrabalho.toMinutes();
      //  long horasTrabalhadas = duracaoTrabalho.toHours();

        long horasExtras = 0;
        if (minutosTrabalhados > 8 * 60) {
            horasExtras = minutosTrabalhados - 8 * 60;
        }

        return new DetailedTimeRecordingDto(
                registroEntrada.getId(),
                registroEntrada.getStartOfWork().toLocalTime(),
                horaSaida.toLocalTime(),
                registroEntrada.getStartOfWork().toLocalDate(),
                horaSaida.toLocalDate(),
                minutosTrabalhados,
                horasExtras
        );
    }



public EmployeeTimeRecordDto buscarRegistrosPontoPorUsuario(Long userId) {
    Employee employee = usuarioRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    List<RecordWorkTime> registros = repository.findByColaborador(employee);

    List<DetailedTimeRecordingDto> registrosDto = registros.stream()
            .map(this::toRegistroPontoDetalhadoDto)
            .collect(Collectors.toList());

    return new EmployeeTimeRecordDto(employee.getName(), registrosDto);
}


    private DetailedTimeRecordingDto toRegistroPontoDetalhadoDto(RecordWorkTime registro) {
        LocalDateTime horaSaida = registro.getEndOfWork() != null ? registro.getEndOfWork() : LocalDateTime.now();

        Duration duracaoTrabalho = Duration.between(registro.getStartOfWork(), horaSaida);
        long minutosTrabalhados = duracaoTrabalho.toMinutes();


        long horasExtras = 0;
        if (minutosTrabalhados > 8 *60 ) {
            horasExtras = minutosTrabalhados - (8 * 60);
        }

        return new DetailedTimeRecordingDto(
                registro.getId(),
                registro.getStartOfWork().toLocalTime(),
                horaSaida.toLocalTime(),
                registro.getStartOfWork().toLocalDate(),
                horaSaida.toLocalDate(),
                minutosTrabalhados,
                horasExtras
        );
    }

    public long calcularHorasExtrasPorIntervaloDeDatas(Long userId, LocalDate dataInicial, LocalDate dataFinal) {
        Employee employee = usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<RecordWorkTime> registros = repository.findByColaboradorAndDateBetween(employee, dataInicial, dataFinal);

        long totalMinutosExtras = 0;

        for (RecordWorkTime registro : registros) {
            long minutosTrabalhados = Duration.between(registro.getStartOfWork(), registro.getEndOfWork()).toMinutes();
            long minutosExtras = Math.max(minutosTrabalhados - (8 * 60), 0); // Convertendo 8 horas para minutos
            totalMinutosExtras += minutosExtras;
        }

        return totalMinutosExtras;
    }
}


