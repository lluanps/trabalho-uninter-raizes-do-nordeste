package com.lluanps.raizes_nordeste_api_uninter.estoque.service;

import com.lluanps.raizes_nordeste_api_uninter.enums.TipoMovimentacaoEstoque;
import com.lluanps.raizes_nordeste_api_uninter.estoque.dto.EstoqueRequest;
import com.lluanps.raizes_nordeste_api_uninter.estoque.dto.EstoqueResponse;
import com.lluanps.raizes_nordeste_api_uninter.estoque.model.Estoque;
import com.lluanps.raizes_nordeste_api_uninter.estoque.model.MovimentacaoEstoque;
import com.lluanps.raizes_nordeste_api_uninter.estoque.repository.EstoqueRepository;
import com.lluanps.raizes_nordeste_api_uninter.estoque.repository.MovimentacaoEstoqueRepository;
import com.lluanps.raizes_nordeste_api_uninter.exceptions.BussinessException;
import com.lluanps.raizes_nordeste_api_uninter.produto.model.Produto;
import com.lluanps.raizes_nordeste_api_uninter.produto.repository.ProdutoRepository;
import com.lluanps.raizes_nordeste_api_uninter.unidade.model.Unidade;
import com.lluanps.raizes_nordeste_api_uninter.unidade.repository.UnidadeRepository;
import com.lluanps.raizes_nordeste_api_uninter.usuario.model.Usuario;
import com.lluanps.raizes_nordeste_api_uninter.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstoqueService {

    @Autowired
    private EstoqueRepository repository;

    @Autowired
    private MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UnidadeRepository unidadeRepository;

    public EstoqueResponse registraMovimentacaoEstoque(Integer unidadeId, EstoqueRequest request, String emailUsuario) {
        Unidade unidade = findUnidadeById(unidadeId);
        Produto produto = findProdutoById(request.getProdutoId());

        Estoque estoque = repository.findByProdutoIdAndUnidadeId(produto.getId(), unidadeId)
                .orElseGet(() -> {Estoque e = Estoque.builder()
                        .produto(produto)
                        .unidade(unidade)
                        .quantidade(0)
                        .build();
                    return repository.save(e);
                });

        if (request.getTipo() == TipoMovimentacaoEstoque.SAIDA) {
            int novaQuantidade = estoque.getQuantidade() - request.getQuantidade();
            if (novaQuantidade < 0) {
                throw new BussinessException("Estoque insuficiente para o produto: " + produto.getNome());
            }

            estoque.setQuantidade(novaQuantidade);
        } else {
            estoque.setQuantidade(estoque.getQuantidade() + request.getQuantidade());
        }

        estoque = repository.save(estoque);

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        MovimentacaoEstoque movimentacaoEstoque = MovimentacaoEstoque.builder()
                .estoque(estoque)
                .usuario(usuario)
                .tipo(request.getTipo())
                .quantidade(request.getQuantidade())
                .observacao(request.getObservacao())
                .build();
        movimentacaoEstoqueRepository.save(movimentacaoEstoque);

        return toResponse(estoque);
    }

    private Produto findProdutoById(Integer id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new BussinessException("Produto não encontrado com id: " + id));
    }

    private Unidade findUnidadeById(Integer id) {
        return unidadeRepository.findById(id)
                .orElseThrow(() -> new BussinessException("Unidade não encontrado com id: " + id));
    }

    public EstoqueResponse consultarEstoque(Integer unidadeId, Integer produtoId) {
        Estoque estoque = repository.findByProdutoIdAndUnidadeId(produtoId, unidadeId)
                .orElseThrow(() -> new BussinessException("Não foi encontrado estoque para o produto de id: " + produtoId));

        return toResponse(estoque);
    }

    public EstoqueResponse toResponse(Estoque estoque) {
        return EstoqueResponse.builder()
                .id(estoque.getId())
                .produtoId(estoque.getProduto().getId())
                .produtoNome(estoque.getProduto().getNome())
                .unidadeId(estoque.getUnidade().getId())
                .unidadeNome(estoque.getUnidade().getNome())
                .quantidade(estoque.getQuantidade())
                .disponivel(estoque.getQuantidade() > 0)
                .atualizadoEm(estoque.getAtualizadoEm())
                .build();
    }

}
