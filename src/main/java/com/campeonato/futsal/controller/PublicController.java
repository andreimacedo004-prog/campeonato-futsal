package com.campeonato.futsal.controller;

import com.campeonato.futsal.model.Divisao;
import com.campeonato.futsal.model.Grupo;
import com.campeonato.futsal.repository.DivisaoRepository;
import com.campeonato.futsal.repository.GrupoRepository;
import com.campeonato.futsal.repository.JogoRepository;
import com.campeonato.futsal.service.ArtilheiroService;
import com.campeonato.futsal.service.ClassificacaoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PublicController {

    private final DivisaoRepository divisaoRepository;
    private final GrupoRepository grupoRepository;
    private final JogoRepository jogoRepository;
    private final ClassificacaoService classificacaoService;
    private final ArtilheiroService artilheiroService;

    public PublicController(DivisaoRepository divisaoRepository, GrupoRepository grupoRepository,
                             JogoRepository jogoRepository, ClassificacaoService classificacaoService,
                             ArtilheiroService artilheiroService) {
        this.divisaoRepository = divisaoRepository;
        this.grupoRepository = grupoRepository;
        this.jogoRepository = jogoRepository;
        this.classificacaoService = classificacaoService;
        this.artilheiroService = artilheiroService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("divisoes", divisaoRepository.findAllByOrderByNomeAsc());
        return "index";
    }

    @GetMapping("/divisao/{id}")
    public String divisao(@PathVariable Long id, Model model) {
        Divisao divisao = divisaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Divisão não encontrada"));
        model.addAttribute("divisao", divisao);
        model.addAttribute("grupos", grupoRepository.findByDivisaoIdOrderByNomeAsc(id));
        return "divisao";
    }

    @GetMapping("/divisao/{id}/artilheiros")
    public String artilheirosDivisao(@PathVariable Long id, Model model) {
        Divisao divisao = divisaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Divisão não encontrada"));
        model.addAttribute("divisao", divisao);
        model.addAttribute("artilheiros", artilheiroService.calcularPorDivisao(id));
        return "artilheiros-divisao";
    }

    @GetMapping("/grupo/{id}")
    public String grupo(@PathVariable Long id, Model model) {
        Grupo grupo = grupoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grupo não encontrado"));

        model.addAttribute("grupo", grupo);
        model.addAttribute("classificacao", classificacaoService.calcular(id));

        List<com.campeonato.futsal.model.Jogo> jogos = jogoRepository.findByGrupoIdOrderByRodadaAscIdAsc(id);
        var jogosPorRodada = jogos.stream()
                .collect(Collectors.groupingBy(
                        j -> j.getRodada() == null ? 0 : j.getRodada(),
                        java.util.TreeMap::new,
                        Collectors.toList()
                ));
        model.addAttribute("jogosPorRodada", jogosPorRodada);

        model.addAttribute("artilheiros", artilheiroService.calcularPorGrupo(id).stream()
                .sorted(Comparator.comparingInt(a -> -a.getTotalGols()))
                .limit(10)
                .toList());

        return "grupo";
    }
}
