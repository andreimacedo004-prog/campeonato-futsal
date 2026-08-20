package com.campeonato.futsal.model;

import jakarta.persistence.*;

@Entity
@Table(name = "gol")
public class Gol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "jogo_id", nullable = false)
    private Jogo jogo;

    @ManyToOne
    @JoinColumn(name = "time_id", nullable = false)
    private Time time;

    @Column(nullable = false)
    private String jogador;

    @Column(nullable = false)
    private Integer quantidade = 1;

    public Gol() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Jogo getJogo() { return jogo; }
    public void setJogo(Jogo jogo) { this.jogo = jogo; }

    public Time getTime() { return time; }
    public void setTime(Time time) { this.time = time; }

    public String getJogador() { return jogador; }
    public void setJogador(String jogador) { this.jogador = jogador; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}
