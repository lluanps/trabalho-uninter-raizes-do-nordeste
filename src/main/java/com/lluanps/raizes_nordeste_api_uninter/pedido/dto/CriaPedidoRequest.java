package com.lluanps.raizes_nordeste_api_uninter.pedido.dto;

import com.lluanps.raizes_nordeste_api_uninter.enums.CanalPedido;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriaPedidoRequest {

    @NotNull(message = "Unidade é obrigatória")
    private Integer unidadeId;

    @NotNull(message = "Canal do pedido é obrigatório")
    private CanalPedido canalPedido;

    @NotEmpty(message = "O pedido deve ter pelo menos um item")
    @Valid
    private List<ItemPedidoRequest> itens;

    private String formaPagamento;

}
