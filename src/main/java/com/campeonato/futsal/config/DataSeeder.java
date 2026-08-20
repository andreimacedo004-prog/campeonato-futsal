package com.campeonato.futsal.config;

import com.campeonato.futsal.model.Divisao;
import com.campeonato.futsal.model.Grupo;
import com.campeonato.futsal.model.Time;
import com.campeonato.futsal.repository.DivisaoRepository;
import com.campeonato.futsal.repository.GrupoRepository;
import com.campeonato.futsal.repository.TimeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final DivisaoRepository divisaoRepository;
    private final GrupoRepository grupoRepository;
    private final TimeRepository timeRepository;

    public DataSeeder(DivisaoRepository divisaoRepository, GrupoRepository grupoRepository, TimeRepository timeRepository) {
        this.divisaoRepository = divisaoRepository;
        this.grupoRepository = grupoRepository;
        this.timeRepository = timeRepository;
    }

    @Override
    public void run(String... args) {
        if (divisaoRepository.count() > 0) {
            return;
        }

        Divisao primeira = divisaoRepository.save(new Divisao("1ª Divisão"));
        criarGrupoComTimes(primeira, "Chave A", 5, 1);
        criarGrupoComTimes(primeira, "Chave B", 5, 6);

        Divisao segunda = divisaoRepository.save(new Divisao("2ª Divisão"));
        // 22 times em 4 chaves: 6 + 6 + 5 + 5
        int proximo = 1;
        proximo = criarGrupoComTimes(segunda, "Chave A", 6, proximo);
        proximo = criarGrupoComTimes(segunda, "Chave B", 6, proximo);
        proximo = criarGrupoComTimes(segunda, "Chave C", 5, proximo);
        criarGrupoComTimes(segunda, "Chave D", 5, proximo);
    }

    private int criarGrupoComTimes(Divisao divisao, String nomeGrupo, int quantidadeTimes, int numeroInicial) {
        Grupo grupo = grupoRepository.save(new Grupo(nomeGrupo, divisao));
        int numero = numeroInicial;
        for (int i = 0; i < quantidadeTimes; i++) {
            timeRepository.save(new Time("Time " + numero, grupo));
            numero++;
        }
        return numero;
    }
}
