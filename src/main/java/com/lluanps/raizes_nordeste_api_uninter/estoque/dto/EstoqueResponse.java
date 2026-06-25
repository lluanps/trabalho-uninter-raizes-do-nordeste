package com.lluanps.raizes_nordeste_api_uninter.estoque.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstoqueResponse {

    private Integer id;
    private Integer produtoId;
    private String produtoNome;
    private Integer unidadeId;
    private String unidadeNome;
    private Integer quantidade;
    private boolean disponivel;
    private LocalDateTime atualizadoEm;

}
