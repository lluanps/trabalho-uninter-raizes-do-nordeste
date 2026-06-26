package com.lluanps.raizes_nordeste_api_uninter.pagamento.dto;

import com.lluanps.raizes_nordeste_api_uninter.enums.StatusPagamento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoResponse {

    private Integer id;
    private Integer pedidoId;
    private BigDecimal valor;
    private String metodoPagamento;
    private StatusPagamento status;
    private String payloadRequisicao;
    private String payloadResposta;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

}
