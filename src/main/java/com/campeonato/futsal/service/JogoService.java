package com.campeonato.futsal.service;

import com.campeonato.futsal.model.Grupo;
import com.campeonato.futsal.model.Jogo;
import com.campeonato.futsal.model.Time;
import com.campeonato.futsal.repository.JogoRepository;
import com.campeonato.futsal.repository.TimeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JogoService {

    private final TimeRepository timeRepository;
    private final JogoRepository jogoRepository;

    public JogoService(TimeRepository timeRepository, JogoRepository jogoRepository) {
        this.timeRepository = timeRepository;
        this.jogoRepository = jogoRepository;
    }

    /**
     * Gera os confrontos de turno unico (cada time enfrenta os outros do
     * grupo uma vez) usando o metodo do circulo, distribuindo em rodadas
     * equilibradas.
     */
    public int gerarTabelaTurnoUnico(Grupo grupo) {
        List<Time> times = timeRepository.findByGrupoIdOrderByNomeAsc(grupo.getId());
        if (times.size() < 2) {
            return 0;
        }

        List<Time> arr = new ArrayList<>(times);
        boolean temBye = arr.size() % 2 != 0;
        if (temBye) {
            arr.add(null); // "time fantasma" para folga quando numero de times e impar
        }

        int n = arr.size();
        int rodadas = n - 1;
        int metade = n / 2;

        List<Jogo> gerados = new ArrayList<>();

        for (int rodada = 0; rodada < rodadas; rodada++) {
            for (int i = 0; i < metade; i++) {
                Time casa = arr.get(i);
                Time fora = arr.get(n - 1 - i);
                if (casa != null && fora != null) {
                    Jogo j = new Jogo();
                    j.setGrupo(grupo);
                    // alterna mando de campo pra nao ficar sempre o mesmo em casa
                    if (rodada % 2 == 0) {
                        j.setTimeCasa(casa);
                        j.setTimeFora(fora);
                    } else {
                        j.setTimeCasa(fora);
                        j.setTimeFora(casa);
                    }
                    j.setRodada(rodada + 1);
                    j.setJogado(false);
                    gerados.add(j);
                }
            }
            // rotaciona mantendo o primeiro fixo
            Time ultimo = arr.remove(n - 1);
            arr.add(1, ultimo);
        }

        jogoRepository.saveAll(gerados);
        return gerados.size();
    }
}
