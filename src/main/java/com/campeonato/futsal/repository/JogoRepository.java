package com.campeonato.futsal.repository;

import com.campeonato.futsal.model.Jogo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JogoRepository extends JpaRepository<Jogo, Long> {
    List<Jogo> findByGrupoIdOrderByRodadaAscIdAsc(Long grupoId);
}
