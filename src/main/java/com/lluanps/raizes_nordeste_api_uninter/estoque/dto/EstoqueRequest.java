package com.lluanps.raizes_nordeste_api_uninter.estoque.dto;

import com.lluanps.raizes_nordeste_api_uninter.enums.TipoMovimentacaoEstoque;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstoqueRequest {

    @NotNull(message = "Produto é obrigatório")
    private Integer produtoId;

    @NotNull(message = "O Tipo da movimentação é obrigatório")
    private TipoMovimentacaoEstoque tipo;

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade minima é 1")
    private Integer quantidade;
    private String observacao;

}
