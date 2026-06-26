package com.lluanps.raizes_nordeste_api_uninter.fidelidade.repository;

import com.lluanps.raizes_nordeste_api_uninter.fidelidade.model.Fidelidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FidelidadeRepository extends JpaRepository<Fidelidade, Integer> {

    Optional<Fidelidade> findByUsuarioId(Integer usuarioId);

}