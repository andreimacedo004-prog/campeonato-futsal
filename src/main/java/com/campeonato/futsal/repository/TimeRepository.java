package com.campeonato.futsal.repository;

import com.campeonato.futsal.model.Time;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeRepository extends JpaRepository<Time, Long> {
    List<Time> findByGrupoIdOrderByNomeAsc(Long grupoId);
}
