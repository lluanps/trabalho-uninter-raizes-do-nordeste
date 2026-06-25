package com.lluanps.raizes_nordeste_api_uninter.produto.mapper;

import com.lluanps.raizes_nordeste_api_uninter.produto.dto.ProdutoResponse;
import com.lluanps.raizes_nordeste_api_uninter.produto.model.Produto;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper {

    public ProdutoResponse toResponse(Produto produto, int quantidade) {
        return ProdutoResponse.builder()
                .id(produto.getId())
                .unidadeId(produto.getUnidade().getId())
                .unidadeNome(produto.getUnidade().getNome())
                .nome(produto.getNome())
                .descricao(produto.getDescricao())
                .preco(produto.getPreco())
                .categoria(produto.getCategoria())
                .imagemUrl(produto.getImagemUrl())
                .ativo(produto.isAtivo())
                .disponivel(quantidade > 0)
                .criadoEm(produto.getCriadoEm())
                .atualizadoEm(produto.getAtualizadoEm())
                .build();
    }
}
