package com.lluanps.raizes_nordeste_api_uninter.estoque.repository;

import com.lluanps.raizes_nordeste_api_uninter.estoque.model.MovimentacaoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Integer> {
}
