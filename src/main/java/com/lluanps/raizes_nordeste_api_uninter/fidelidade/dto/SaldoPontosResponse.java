package com.lluanps.raizes_nordeste_api_uninter.fidelidade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaldoPontosResponse {

    private Integer usuarioId;
    private Integer saldoPontos;
    private boolean consentimentoAtivo;
    private List<MovimentacaoPontosResponse> historicoMovimentacaoPontos;

}
