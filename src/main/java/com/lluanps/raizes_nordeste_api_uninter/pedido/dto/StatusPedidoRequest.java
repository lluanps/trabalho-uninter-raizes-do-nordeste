package com.lluanps.raizes_nordeste_api_uninter.pedido.dto;

import com.lluanps.raizes_nordeste_api_uninter.enums.StatusPedido;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusPedidoRequest {

    @NotNull(message = "Novo status é obrigatório")
    private StatusPedido novoStatus;

}
