package com.campeonato.futsal.service;

import com.campeonato.futsal.dto.Artilheiro;
import com.campeonato.futsal.model.Gol;
import com.campeonato.futsal.repository.GolRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArtilheiroService {

    private final GolRepository golRepository;

    public ArtilheiroService(GolRepository golRepository) {
        this.golRepository = golRepository;
    }

    public List<Artilheiro> calcularPorGrupo(Long grupoId) {
        return calcular(golRepository.findByJogo_Grupo_Id(grupoId));
    }

    public List<Artilheiro> calcularPorDivisao(Long divisaoId) {
        return calcular(golRepository.findByJogo_Grupo_Divisao_Id(divisaoId));
    }

    private List<Artilheiro> calcular(List<Gol> gols) {
        Map<String, Artilheiro> mapa = new LinkedHashMap<>();
        for (Gol g : gols) {
            String chave = g.getJogador().trim().toLowerCase() + "|" + g.getTime().getId();
            Artilheiro a = mapa.get(chave);
            if (a == null) {
                a = new Artilheiro(g.getJogador(), g.getTime().getNome(), 0);
                mapa.put(chave, a);
            }
            a.somar(g.getQuantidade() == null ? 1 : g.getQuantidade());
        }
        return mapa.values().stream()
                .sorted(Comparator.comparingInt(Artilheiro::getTotalGols).reversed()
                        .thenComparing(Artilheiro::getJogador))
                .toList();
    }
}
