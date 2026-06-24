package com.lluanps.raizes_nordeste_api_uninter.unidade.repository;

import com.lluanps.raizes_nordeste_api_uninter.unidade.model.Unidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnidadeRepository extends JpaRepository<Unidade, Integer> {
}
