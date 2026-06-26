package com.lluanps.raizes_nordeste_api_uninter.fidelidade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResgatePontosResponse {

    private Integer pontosResgatados;
    private Integer saldoRestante;
    private BigDecimal descontoAplicado;
    private String mensagem;

}
