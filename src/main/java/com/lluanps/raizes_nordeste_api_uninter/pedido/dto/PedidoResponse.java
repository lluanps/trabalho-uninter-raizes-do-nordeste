package com.lluanps.raizes_nordeste_api_uninter.pedido.dto;

import com.lluanps.raizes_nordeste_api_uninter.enums.CanalPedido;
import com.lluanps.raizes_nordeste_api_uninter.enums.StatusPedido;
import com.lluanps.raizes_nordeste_api_uninter.pedido.dto.ItemPedidoResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponse {

    private Integer id;
    private Integer usuarioId;
    private String usuarioNome;
    private Integer unidadeId;
    private String unidadeNome;
    private StatusPedido status;
    private CanalPedido canal;
    private BigDecimal valorTotal;
    private BigDecimal desconto;
    private Integer pontosUtilizados;
    private List<ItemPedidoResponse> itens;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

}
