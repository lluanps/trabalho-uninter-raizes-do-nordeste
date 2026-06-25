package com.lluanps.raizes_nordeste_api_uninter.pedido.repository;

import com.lluanps.raizes_nordeste_api_uninter.pedido.model.HistoricoStatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoRepository extends JpaRepository<HistoricoStatusPedido, Long> {

    List<HistoricoStatusPedido> findByPedidoId(Integer pedidoId);
}
