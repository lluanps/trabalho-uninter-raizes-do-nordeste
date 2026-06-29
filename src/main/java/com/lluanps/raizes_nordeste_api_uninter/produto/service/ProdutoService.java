package com.lluanps.raizes_nordeste_api_uninter.produto.service;

import com.lluanps.raizes_nordeste_api_uninter.estoque.model.Estoque;
import com.lluanps.raizes_nordeste_api_uninter.estoque.repository.EstoqueRepository;
import com.lluanps.raizes_nordeste_api_uninter.exceptions.BussinessException;
import com.lluanps.raizes_nordeste_api_uninter.exceptions.NotFoundException;
import com.lluanps.raizes_nordeste_api_uninter.produto.dto.ProdutoRequest;
import com.lluanps.raizes_nordeste_api_uninter.produto.dto.ProdutoResponse;
import com.lluanps.raizes_nordeste_api_uninter.produto.mapper.ProdutoMapper;
import com.lluanps.raizes_nordeste_api_uninter.produto.model.Produto;
import com.lluanps.raizes_nordeste_api_uninter.produto.repository.ProdutoRepository;
import com.lluanps.raizes_nordeste_api_uninter.unidade.model.Unidade;
import com.lluanps.raizes_nordeste_api_uninter.unidade.repository.UnidadeRepository;
import com.lluanps.raizes_nordeste_api_uninter.usuario.dto.UsuarioResponse;
import com.lluanps.raizes_nordeste_api_uninter.usuario.model.Usuario;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private UnidadeRepository unidadeRepository;

    @Autowired
    private ProdutoMapper produtoMapper;

    public ProdutoResponse criarProduto(Integer unidadeId, ProdutoRequest request) {
        Unidade unidade = findUnidadeById(unidadeId);

        Produto produto = Produto.builder()
                .unidade(unidade)
                .nome(request.getNome())
                .descricao(request.getDescricao())
                .preco(request.getPreco())
                .categoria(request.getCategoria())
                .imagemUrl(request.getImagemUrl())
                .ativo(true)
                .build();

        produto = repository.save(produto);

        Estoque estoque = Estoque.builder()
                .produto(produto)
                .unidade(unidade)
                .quantidade(0)
                .build();
        estoqueRepository.save(estoque);

        return produtoMapper.toResponse(produto, 0);
    }

    public ProdutoResponse atualizar(Integer unidadeId, Integer produtoId, ProdutoRequest request) {
        Produto produto = findProdutoAtivoByIdAndUnidade(produtoId, unidadeId);

        produto.setNome(request.getNome());
        produto.setDescricao(request.getDescricao());
        produto.setPreco(request.getPreco());
        produto.setCategoria(request.getCategoria());
        produto.setImagemUrl(request.getImagemUrl());

        produto = repository.save(produto);

        int quantidade = getQuantidade(produto.getId(), unidadeId);
        return produtoMapper.toResponse(produto, quantidade);
    }

    @Transactional
    public void remover(Integer unidadeId, Integer produtoId) {
        Produto produto = findProdutoAtivoByIdAndUnidade(produtoId, unidadeId);
        produto.setAtivo(false);
        repository.save(produto);
    }

    @Transactional(readOnly = true)
    public Page<ProdutoResponse> listarUnidades(Integer unidadeId, Pageable pageable) {
        return repository.findByUnidadeIdAndAtivoTrue(unidadeId, pageable)
                .map(p -> {
                    int quantidade = getQuantidade(p.getId(), unidadeId);
                    return produtoMapper.toResponse(p, quantidade);
                });
    }

    private int getQuantidade(Integer produtoId, Integer unidadeId) {
        return estoqueRepository.findByProdutoIdAndUnidadeId(produtoId, unidadeId)
                .map(Estoque::getQuantidade)
                .orElse(0);
    }

    private Produto findProdutoAtivoByIdAndUnidade(Integer produtoId, Integer unidadeId) {
        return repository.findByIdAndUnidadeIdAndAtivoTrue(produtoId, unidadeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Produto não encontrado com id: " + produtoId));
    }

    private Unidade findUnidadeById(Integer unidadeId) {
        return unidadeRepository.findById(unidadeId)
                .orElseThrow(() -> new NotFoundException("Unidade não encontrada com id: " + unidadeId));
    }

}
