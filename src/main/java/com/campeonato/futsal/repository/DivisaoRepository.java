package com.campeonato.futsal.repository;

import com.campeonato.futsal.model.Divisao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DivisaoRepository extends JpaRepository<Divisao, Long> {
    List<Divisao> findAllByOrderByNomeAsc();
}
