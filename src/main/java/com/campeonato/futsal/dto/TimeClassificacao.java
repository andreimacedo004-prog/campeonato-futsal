package com.campeonato.futsal.dto;

import com.campeonato.futsal.model.Time;

public class TimeClassificacao {

    private final Time time;
    private int jogos = 0;
    private int vitorias = 0;
    private int empates = 0;
    private int derrotas = 0;
    private int golsPro = 0;
    private int golsContra = 0;

    public TimeClassificacao(Time time) {
        this.time = time;
    }

    public Time getTime() { return time; }

    public int getJogos() { return jogos; }
    public int getVitorias() { return vitorias; }
    public int getEmpates() { return empates; }
    public int getDerrotas() { return derrotas; }
    public int getGolsPro() { return golsPro; }
    public int getGolsContra() { return golsContra; }

    public int getSaldoGols() { return golsPro - golsContra; }
    public int getPontos() { return vitorias * 3 + empates; }

    public void registrarJogo(int golsFeitos, int golsSofridos) {
        jogos++;
        golsPro += golsFeitos;
        golsContra += golsSofridos;
        if (golsFeitos > golsSofridos) {
            vitorias++;
        } else if (golsFeitos == golsSofridos) {
            empates++;
        } else {
            derrotas++;
        }
    }
}
