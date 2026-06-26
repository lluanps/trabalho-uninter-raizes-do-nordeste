package com.lluanps.raizes_nordeste_api_uninter.fidelidade.repository;

import com.lluanps.raizes_nordeste_api_uninter.fidelidade.model.MovimentacaoPontosFidelidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimentacaoPontosRepository extends JpaRepository<MovimentacaoPontosFidelidade, Integer> {

    List<MovimentacaoPontosFidelidade> findByProgramaFidelidadeId(Integer programaFidelidadeId);
}
