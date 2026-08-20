package com.campeonato.futsal.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "jogo")
public class Jogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "grupo_id", nullable = false)
    private Grupo grupo;

    @ManyToOne
    @JoinColumn(name = "time_casa_id", nullable = false)
    private Time timeCasa;

    @ManyToOne
    @JoinColumn(name = "time_fora_id", nullable = false)
    private Time timeFora;

    private Integer golsCasa;
    private Integer golsFora;

    private boolean jogado = false;

    private LocalDate data;

    private Integer rodada;

    @OneToMany(mappedBy = "jogo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Gol> gols = new ArrayList<>();

    public Jogo() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Grupo getGrupo() { return grupo; }
    public void setGrupo(Grupo grupo) { this.grupo = grupo; }

    public Time getTimeCasa() { return timeCasa; }
    public void setTimeCasa(Time timeCasa) { this.timeCasa = timeCasa; }

    public Time getTimeFora() { return timeFora; }
    public void setTimeFora(Time timeFora) { this.timeFora = timeFora; }

    public Integer getGolsCasa() { return golsCasa; }
    public void setGolsCasa(Integer golsCasa) { this.golsCasa = golsCasa; }

    public Integer getGolsFora() { return golsFora; }
    public void setGolsFora(Integer golsFora) { this.golsFora = golsFora; }

    public boolean isJogado() { return jogado; }
    public void setJogado(boolean jogado) { this.jogado = jogado; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public Integer getRodada() { return rodada; }
    public void setRodada(Integer rodada) { this.rodada = rodada; }

    public List<Gol> getGols() { return gols; }
    public void setGols(List<Gol> gols) { this.gols = gols; }
}
