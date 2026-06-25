package com.lluanps.raizes_nordeste_api_uninter.estoque.repository;

import com.lluanps.raizes_nordeste_api_uninter.estoque.model.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Integer> {

    Optional<Estoque> findByProdutoIdAndUnidadeId(Integer produtoId, Integer unidadeId);
}