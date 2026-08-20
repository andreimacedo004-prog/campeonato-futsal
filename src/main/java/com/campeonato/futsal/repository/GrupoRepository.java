package com.campeonato.futsal.repository;

import com.campeonato.futsal.model.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {
    List<Grupo> findByDivisaoIdOrderByNomeAsc(Long divisaoId);
}
