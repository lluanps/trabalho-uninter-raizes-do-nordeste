package com.lluanps.raizes_nordeste_api_uninter.produto.dto;

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
public class ProdutoResponse {

    private Integer id;
    private Integer unidadeId;
    private String unidadeNome;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private String categoria;
    private String imagemUrl;
    private boolean ativo;
    private boolean disponivel;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

}
