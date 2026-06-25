package com.lluanps.raizes_nordeste_api_uninter.produto.controller;

import com.lluanps.raizes_nordeste_api_uninter.produto.dto.ProdutoRequest;
import com.lluanps.raizes_nordeste_api_uninter.produto.dto.ProdutoResponse;
import com.lluanps.raizes_nordeste_api_uninter.produto.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/unidades/{unidadeId}/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @PostMapping
    public ResponseEntity<ProdutoResponse> criarProduto(@PathVariable Integer unidadeId,
            @Valid @RequestBody ProdutoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarProduto(unidadeId, request));
    }

    @GetMapping
    public ResponseEntity<Page<ProdutoResponse>> listarProdutos(@PathVariable Integer unidadeId, Pageable pageable) {
        return ResponseEntity.ok(service.listarUnidades(unidadeId, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(@PathVariable Integer unidadeId, @PathVariable Integer id,
            @Valid @RequestBody ProdutoRequest request) {
        return ResponseEntity.ok(service.atualizar(unidadeId, id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarProduto(@PathVariable Integer unidadeId, @PathVariable Integer id) {
        service.remover(unidadeId, id);
    }
}
