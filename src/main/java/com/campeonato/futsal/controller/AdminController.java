package com.campeonato.futsal.controller;

import com.campeonato.futsal.model.*;
import com.campeonato.futsal.repository.*;
import com.campeonato.futsal.service.JogoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final DivisaoRepository divisaoRepository;
    private final GrupoRepository grupoRepository;
    private final TimeRepository timeRepository;
    private final JogoRepository jogoRepository;
    private final GolRepository golRepository;
    private final JogoService jogoService;

    public AdminController(DivisaoRepository divisaoRepository, GrupoRepository grupoRepository,
                            TimeRepository timeRepository, JogoRepository jogoRepository,
                            GolRepository golRepository, JogoService jogoService) {
        this.divisaoRepository = divisaoRepository;
        this.grupoRepository = grupoRepository;
        this.timeRepository = timeRepository;
        this.jogoRepository = jogoRepository;
        this.golRepository = golRepository;
        this.jogoService = jogoService;
    }

    // ---------- login ----------

    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }

    // ---------- dashboard ----------

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("divisoes", divisaoRepository.findAllByOrderByNomeAsc());
        return "admin/dashboard";
    }

    // ---------- grupos ----------

    @GetMapping("/divisao/{divisaoId}/grupos/novo")
    public String novoGrupoForm(@PathVariable Long divisaoId, Model model) {
        Divisao divisao = getDivisao(divisaoId);
        model.addAttribute("divisao", divisao);
        return "admin/grupo-form";
    }

    @PostMapping("/divisao/{divisaoId}/grupos")
    public String salvarGrupo(@PathVariable Long divisaoId, @RequestParam String nome) {
        Divisao divisao = getDivisao(divisaoId);
        grupoRepository.save(new Grupo(nome, divisao));
        return "redirect:/admin";
    }

    @PostMapping("/grupos/{id}/excluir")
    public String excluirGrupo(@PathVariable Long id) {
        grupoRepository.deleteById(id);
        return "redirect:/admin";
    }

    // ---------- times ----------

    @GetMapping("/grupos/{grupoId}/times/novo")
    public String novoTimeForm(@PathVariable Long grupoId, Model model) {
        Grupo grupo = getGrupo(grupoId);
        model.addAttribute("grupo", grupo);
        return "admin/time-form";
    }

    @PostMapping("/grupos/{grupoId}/times")
    public String salvarTime(@PathVariable Long grupoId, @RequestParam String nome) {
        Grupo grupo = getGrupo(grupoId);
        timeRepository.save(new Time(nome, grupo));
        return "redirect:/admin/grupos/" + grupoId;
    }

    @GetMapping("/grupos/{id}")
    public String verGrupoAdmin(@PathVariable Long id, Model model) {
        Grupo grupo = getGrupo(id);
        model.addAttribute("grupo", grupo);
        model.addAttribute("times", timeRepository.findByGrupoIdOrderByNomeAsc(id));
        model.addAttribute("jogos", jogoRepository.findByGrupoIdOrderByRodadaAscIdAsc(id));
        return "admin/grupo-detalhe";
    }

    @GetMapping("/times/{id}/editar")
    public String editarTimeForm(@PathVariable Long id, Model model) {
        model.addAttribute("time", getTime(id));
        return "admin/time-editar";
    }

    @PostMapping("/times/{id}/editar")
    public String editarTime(@PathVariable Long id, @RequestParam String nome) {
        Time time = getTime(id);
        time.setNome(nome);
        timeRepository.save(time);
        return "redirect:/admin/grupos/" + time.getGrupo().getId();
    }

    @PostMapping("/times/{id}/excluir")
    public String excluirTime(@PathVariable Long id) {
        Time time = getTime(id);
        Long grupoId = time.getGrupo().getId();
        timeRepository.deleteById(id);
        return "redirect:/admin/grupos/" + grupoId;
    }

    // ---------- jogos ----------

    @PostMapping("/grupos/{grupoId}/jogos/gerar")
    public String gerarTabela(@PathVariable Long grupoId) {
        Grupo grupo = getGrupo(grupoId);
        jogoService.gerarTabelaTurnoUnico(grupo);
        return "redirect:/admin/grupos/" + grupoId;
    }

    @GetMapping("/jogos/{id}/editar")
    public String editarJogoForm(@PathVariable Long id, Model model) {
        Jogo jogo = getJogo(id);
        model.addAttribute("jogo", jogo);
        model.addAttribute("gols", golRepository.findByJogoId(id));
        return "admin/jogo-form";
    }

    @PostMapping("/jogos/{id}/editar")
    public String editarJogo(@PathVariable Long id,
                              @RequestParam(required = false) String golsCasa,
                              @RequestParam(required = false) String golsFora,
                              @RequestParam(required = false) String data,
                              @RequestParam(required = false) String rodada,
                              @RequestParam(defaultValue = "false") boolean jogado) {
        Jogo jogo = getJogo(id);
        jogo.setGolsCasa(paraInteiro(golsCasa));
        jogo.setGolsFora(paraInteiro(golsFora));
        jogo.setRodada(paraInteiro(rodada));
        jogo.setJogado(jogado);
        if (data != null && !data.isBlank()) {
            jogo.setData(LocalDate.parse(data));
        } else {
            jogo.setData(null);
        }
        jogoRepository.save(jogo);
        return "redirect:/admin/jogos/" + id + "/editar";
    }

    private Integer paraInteiro(String valor) {
        if (valor == null || valor.isBlank()) return null;
        try {
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @PostMapping("/jogos/{id}/gols")
    public String adicionarGol(@PathVariable Long id,
                                @RequestParam Long timeId,
                                @RequestParam String jogador,
                                @RequestParam(defaultValue = "1") Integer quantidade) {
        Jogo jogo = getJogo(id);
        Time time = getTime(timeId);
        Gol gol = new Gol();
        gol.setJogo(jogo);
        gol.setTime(time);
        gol.setJogador(jogador);
        gol.setQuantidade(quantidade);
        golRepository.save(gol);
        return "redirect:/admin/jogos/" + id + "/editar";
    }

    @PostMapping("/gols/{id}/excluir")
    public String excluirGol(@PathVariable Long id) {
        Gol gol = golRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gol não encontrado"));
        Long jogoId = gol.getJogo().getId();
        golRepository.deleteById(id);
        return "redirect:/admin/jogos/" + jogoId + "/editar";
    }

    // ---------- helpers ----------

    private Divisao getDivisao(Long id) {
        return divisaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Divisão não encontrada"));
    }

    private Grupo getGrupo(Long id) {
        return grupoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grupo não encontrado"));
    }

    private Time getTime(Long id) {
        return timeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Time não encontrado"));
    }

    private Jogo getJogo(Long id) {
        return jogoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jogo não encontrado"));
    }
}
