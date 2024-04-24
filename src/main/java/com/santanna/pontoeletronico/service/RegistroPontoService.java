package com.santanna.pontoeletronico.service;

import com.santanna.pontoeletronico.domain.dto.RegistroPontoDetalhadoDto;
import com.santanna.pontoeletronico.domain.dto.RegistroPontoDto;
import com.santanna.pontoeletronico.domain.dto.RegistroPontoUsuarioDto;
import com.santanna.pontoeletronico.domain.entity.RegistroPonto;
import com.santanna.pontoeletronico.domain.entity.Usuario;
import com.santanna.pontoeletronico.repository.RegistroPontoRepository;

import com.santanna.pontoeletronico.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class RegistroPontoService {

    @Autowired
    private RegistroPontoRepository repository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService; // Se você tiver um serviço de usuário

    public RegistroPontoDto registrarEntrada(Long userId) {
        RegistroPonto registroAtivo = repository.findRegistroEntradaAtivo(userId);

        if (registroAtivo != null && registroAtivo.getSaida() == null) {
            // Já existe um registro de entrada ativo, atualiza a hora de saída
            registroAtivo.setSaida(LocalDateTime.now());
            repository.save(registroAtivo);
        }

        // Cria um novo registro de entrada
        RegistroPonto novoRegistro = new RegistroPonto();
        // Obtém o usuário pelo ID
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        novoRegistro.setUsuario(usuario);
        novoRegistro.setEntrada(LocalDateTime.now());
        repository.save(novoRegistro);

        return new RegistroPontoDto(novoRegistro);
    }


//    public RegistroPontoDto registrarSaida(Long userId) {
//        Usuario usuario = usuarioRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
//
//        // Encontra o último registro de ponto de entrada para o usuário
//        RegistroPonto registroEntrada = repository.findTopByUsuarioAndSaidaIsNullOrderByEntradaDesc(usuario);
//
//        if (registroEntrada == null) {
//            throw new RuntimeException("Não há registro de entrada para o usuário.");
//        }
//
//        // Registra a saída e calcula o tempo decorrido em minutos
//        LocalDateTime horaSaida = LocalDateTime.now();
//        registroEntrada.setSaida(horaSaida);
//        repository.save(registroEntrada);
//
//        long minutosTrabalhados = Duration.between(registroEntrada.getEntrada(), horaSaida).toMinutes();
//
//        return new RegistroPontoDto(registroEntrada.getId(), registroEntrada.getEntrada(), registroEntrada.getSaida(), minutosTrabalhados);
//    }

    public RegistroPontoDetalhadoDto registrarSaida(Long userId) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Encontra o último registro de ponto de entrada para o usuário
        RegistroPonto registroEntrada = repository.findTopByUsuarioAndSaidaIsNullOrderByEntradaDesc(usuario);

        if (registroEntrada == null) {
            throw new RuntimeException("Não há registro de entrada para o usuário.");
        }

        // Registra a saída e calcula as horas trabalhadas e as horas extras
        LocalDateTime horaSaida = LocalDateTime.now();
        registroEntrada.setSaida(horaSaida);
        repository.save(registroEntrada);

        Duration duracaoTrabalho = Duration.between(registroEntrada.getEntrada(), horaSaida);
        long minutosTrabalhados = duracaoTrabalho.toMinutes();
        long horasTrabalhadas = duracaoTrabalho.toHours();

        long horasExtras = 0;
        if (horasTrabalhadas > 8) {
            horasExtras = horasTrabalhadas - 8;
        }

        return new RegistroPontoDetalhadoDto(
                registroEntrada.getId(),
                registroEntrada.getEntrada().toLocalTime(),
                horaSaida.toLocalTime(),
                registroEntrada.getEntrada().toLocalDate(),
                horaSaida.toLocalDate(),
                horasTrabalhadas,
                horasExtras
        );
    }


//    public long calcularTotalMinutosTrabalhados(Long userId) {
//        Usuario usuario = usuarioRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
//
//        // Obtém todos os registros de ponto do usuário
//        List<RegistroPonto> registros = repository.findByUsuario(usuario);
//
//        // Inicializa a variável para armazenar o total de minutos trabalhados
//        long totalMinutosTrabalhados = 0;
//
//        // Itera sobre os registros de ponto e acumula os minutos trabalhados de cada registro
//        for (RegistroPonto registro : registros) {
//            if (registro.getSaida() != null) {
//                // Calcula os minutos trabalhados no registro
//                long minutosTrabalhados = Duration.between(registro.getEntrada(), registro.getSaida()).toMinutes();
//                // Acumula os minutos trabalhados no total
//                totalMinutosTrabalhados += minutosTrabalhados;
//            }
//        }
//
//        return totalMinutosTrabalhados;
//    }
public RegistroPontoUsuarioDto buscarRegistrosPontoPorUsuario(Long userId) {
    Usuario usuario = usuarioRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    List<RegistroPonto> registros = repository.findByUsuario(usuario);

    List<RegistroPontoDetalhadoDto> registrosDto = registros.stream()
            .map(this::toRegistroPontoDetalhadoDto)
            .collect(Collectors.toList());

    return new RegistroPontoUsuarioDto(usuario.getName(), registrosDto);
}
    private RegistroPontoDetalhadoDto toRegistroPontoDetalhadoDto(RegistroPonto registro) {
        LocalDateTime horaSaida = registro.getSaida() != null ? registro.getSaida() : LocalDateTime.now();

        Duration duracaoTrabalho = Duration.between(registro.getEntrada(), horaSaida);
        long minutosTrabalhados = duracaoTrabalho.toMinutes();
        long horasTrabalhadas = duracaoTrabalho.toHours();

        long horasExtras = 0;
        if (horasTrabalhadas > 8) {
            horasExtras = horasTrabalhadas - 8;
        }

        return new RegistroPontoDetalhadoDto(
                registro.getId(),
                registro.getEntrada().toLocalTime(),
                horaSaida.toLocalTime(),
                registro.getEntrada().toLocalDate(),
                horaSaida.toLocalDate(),
                horasTrabalhadas,
                horasExtras
        );
    }

    public long calcularHorasExtrasPorIntervaloDeDatas(Long userId, LocalDate dataInicial, LocalDate dataFinal) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<RegistroPonto> registros = repository.findByUsuarioAndDataBetween(usuario, dataInicial, dataFinal);

        long totalHorasExtras = 0;

        for (RegistroPonto registro : registros) {
            long horasTrabalhadas = Duration.between(registro.getEntrada(), registro.getSaida()).toHours();
            long horasExtras = Math.max(horasTrabalhadas - 8, 0);
            totalHorasExtras += horasExtras;
        }

        return totalHorasExtras;
    }
}


