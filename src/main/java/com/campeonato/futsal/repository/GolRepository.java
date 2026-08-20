package com.campeonato.futsal.repository;

import com.campeonato.futsal.model.Gol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GolRepository extends JpaRepository<Gol, Long> {
    List<Gol> findByJogoId(Long jogoId);
    List<Gol> findByJogo_Grupo_Id(Long grupoId);
    List<Gol> findByJogo_Grupo_Divisao_Id(Long divisaoId);
}
