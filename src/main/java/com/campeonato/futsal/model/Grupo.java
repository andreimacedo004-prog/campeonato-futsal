package com.campeonato.futsal.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "grupo")
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome; // ex: "Chave A"

    @ManyToOne
    @JoinColumn(name = "divisao_id", nullable = false)
    private Divisao divisao;

    @OneToMany(mappedBy = "grupo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Time> times = new ArrayList<>();

    @OneToMany(mappedBy = "grupo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Jogo> jogos = new ArrayList<>();

    public Grupo() {}

    public Grupo(String nome, Divisao divisao) {
        this.nome = nome;
        this.divisao = divisao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Divisao getDivisao() { return divisao; }
    public void setDivisao(Divisao divisao) { this.divisao = divisao; }

    public List<Time> getTimes() { return times; }
    public void setTimes(List<Time> times) { this.times = times; }

    public List<Jogo> getJogos() { return jogos; }
    public void setJogos(List<Jogo> jogos) { this.jogos = jogos; }
}
