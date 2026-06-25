package com.lluanps.raizes_nordeste_api_uninter.produto.repository;

import com.lluanps.raizes_nordeste_api_uninter.produto.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

    @Query("SELECT p FROM Produto p JOIN FETCH p.unidade " +
            "WHERE p.unidade.id = :unidadeId AND p.ativo = true")
    Page<Produto> findByUnidadeIdAndAtivoTrue(@Param("unidadeId") Integer unidadeId, Pageable pageable);

    @Query("SELECT p FROM Produto p JOIN FETCH p.unidade " +
            "WHERE p.id =:id AND p.unidade.id = :unidadeId AND p.ativo = true")
    Optional<Produto> findByIdAndUnidadeIdAndAtivoTrue(@Param("id") Integer id, @Param("unidadeId") Integer unidadeId);

}
