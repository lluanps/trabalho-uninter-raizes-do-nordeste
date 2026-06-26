package com.lluanps.raizes_nordeste_api_uninter.pagamento.repository;

import com.lluanps.raizes_nordeste_api_uninter.pagamento.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {

    Optional<Pagamento> findTopByPedidoIdOrderByCriadoEmDesc(Integer pedidoId);

}
