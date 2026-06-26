package com.lluanps.raizes_nordeste_api_uninter.fidelidade.dto;

import com.lluanps.raizes_nordeste_api_uninter.enums.TipoMovimentacaoPontos;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimentacaoPontosResponse {

    private Integer id;
    private TipoMovimentacaoPontos tipo;
    private Integer pontos;
    private String descricao;
    private Integer pedidoId;
    private LocalDateTime criadoEm;
}
