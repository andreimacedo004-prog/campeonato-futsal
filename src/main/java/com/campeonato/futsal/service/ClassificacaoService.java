package com.campeonato.futsal.service;

import com.campeonato.futsal.dto.TimeClassificacao;
import com.campeonato.futsal.model.Jogo;
import com.campeonato.futsal.model.Time;
import com.campeonato.futsal.repository.JogoRepository;
import com.campeonato.futsal.repository.TimeRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClassificacaoService {

    private final TimeRepository timeRepository;
    private final JogoRepository jogoRepository;

    public ClassificacaoService(TimeRepository timeRepository, JogoRepository jogoRepository) {
        this.timeRepository = timeRepository;
        this.jogoRepository = jogoRepository;
    }

    public List<TimeClassificacao> calcular(Long grupoId) {
        List<Time> times = timeRepository.findByGrupoIdOrderByNomeAsc(grupoId);

        Map<Long, TimeClassificacao> mapa = new LinkedHashMap<>();
        for (Time t : times) {
            mapa.put(t.getId(), new TimeClassificacao(t));
        }

        List<Jogo> jogos = jogoRepository.findByGrupoIdOrderByRodadaAscIdAsc(grupoId);
        for (Jogo j : jogos) {
            if (!j.isJogado() || j.getGolsCasa() == null || j.getGolsFora() == null) continue;

            TimeClassificacao casa = mapa.get(j.getTimeCasa().getId());
            TimeClassificacao fora = mapa.get(j.getTimeFora().getId());
            if (casa == null || fora == null) continue;

            casa.registrarJogo(j.getGolsCasa(), j.getGolsFora());
            fora.registrarJogo(j.getGolsFora(), j.getGolsCasa());
        }

        return mapa.values().stream()
                .sorted(
                        Comparator.comparingInt(TimeClassificacao::getPontos).reversed()
                                .thenComparing(Comparator.comparingInt(TimeClassificacao::getSaldoGols).reversed())
                                .thenComparing(Comparator.comparingInt(TimeClassificacao::getGolsPro).reversed())
                                .thenComparing(tc -> tc.getTime().getNome())
                )
                .toList();
    }
}
