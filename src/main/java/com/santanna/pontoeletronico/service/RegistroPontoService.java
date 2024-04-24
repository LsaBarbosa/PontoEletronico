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
import java.util.List;
import java.util.stream.Collectors;


@Service
public class RegistroPontoService {

    @Autowired
    private RegistroPontoRepository repository;
    @Autowired
    private UsuarioRepository usuarioRepository;


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
      //  long horasTrabalhadas = duracaoTrabalho.toHours();

        long horasExtras = 0;
        if (minutosTrabalhados > 8 * 60) {
            horasExtras = minutosTrabalhados - 8 * 60;
        }

        return new RegistroPontoDetalhadoDto(
                registroEntrada.getId(),
                registroEntrada.getEntrada().toLocalTime(),
                horaSaida.toLocalTime(),
                registroEntrada.getEntrada().toLocalDate(),
                horaSaida.toLocalDate(),
                minutosTrabalhados,
                horasExtras
        );
    }



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


        long horasExtras = 0;
        if (minutosTrabalhados > 8 *60 ) {
            horasExtras = minutosTrabalhados - (8 * 60);
        }

        return new RegistroPontoDetalhadoDto(
                registro.getId(),
                registro.getEntrada().toLocalTime(),
                horaSaida.toLocalTime(),
                registro.getEntrada().toLocalDate(),
                horaSaida.toLocalDate(),
                minutosTrabalhados,
                horasExtras
        );
    }

    public long calcularHorasExtrasPorIntervaloDeDatas(Long userId, LocalDate dataInicial, LocalDate dataFinal) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<RegistroPonto> registros = repository.findByUsuarioAndDataBetween(usuario, dataInicial, dataFinal);

        long totalMinutosExtras = 0;

        for (RegistroPonto registro : registros) {
            long minutosTrabalhados = Duration.between(registro.getEntrada(), registro.getSaida()).toMinutes();
            long minutosExtras = Math.max(minutosTrabalhados - (8 * 60), 0); // Convertendo 8 horas para minutos
            totalMinutosExtras += minutosExtras;
        }

        return totalMinutosExtras;
    }
}


