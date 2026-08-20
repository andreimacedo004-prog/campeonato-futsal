package com.campeonato.futsal.dto;

public class Artilheiro {

    private final String jogador;
    private final String timeNome;
    private int totalGols;

    public Artilheiro(String jogador, String timeNome, int totalGols) {
        this.jogador = jogador;
        this.timeNome = timeNome;
        this.totalGols = totalGols;
    }

    public String getJogador() { return jogador; }
    public String getTimeNome() { return timeNome; }
    public int getTotalGols() { return totalGols; }
    public void somar(int quantidade) { this.totalGols += quantidade; }
}
