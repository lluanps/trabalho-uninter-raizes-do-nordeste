package com.lluanps.raizes_nordeste_api_uninter.pedido.repository;

import com.lluanps.raizes_nordeste_api_uninter.enums.CanalPedido;
import com.lluanps.raizes_nordeste_api_uninter.enums.StatusPedido;
import com.lluanps.raizes_nordeste_api_uninter.pedido.model.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    Page<Pedido> findByUsuarioId(Integer usuarioId, Pageable pageable);
    Page<Pedido> findByCanal(CanalPedido canal, Pageable pageable);
    Page<Pedido> findByStatus(StatusPedido status, Pageable pageable);
    Page<Pedido> findByUnidadeId(Integer unidadeId, Pageable pageable);

}
