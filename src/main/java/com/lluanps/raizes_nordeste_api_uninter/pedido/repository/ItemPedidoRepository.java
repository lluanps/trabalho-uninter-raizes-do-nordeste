package com.lluanps.raizes_nordeste_api_uninter.pedido.repository;

import com.lluanps.raizes_nordeste_api_uninter.pedido.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {

    List<ItemPedido> findByPedidoId(Integer pedidoId);

}

