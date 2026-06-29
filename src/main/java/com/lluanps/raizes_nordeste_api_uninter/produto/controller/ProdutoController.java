package com.lluanps.raizes_nordeste_api_uninter.produto.controller;

import com.lluanps.raizes_nordeste_api_uninter.produto.dto.ProdutoRequest;
import com.lluanps.raizes_nordeste_api_uninter.produto.dto.ProdutoResponse;
import com.lluanps.raizes_nordeste_api_uninter.produto.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Produtos", description = "Cardápio por unidade")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/unidades/{unidadeId}/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @Operation(summary = "Registra um novo produto na unidade", description = "ADMIN e GERENTE gerenciam o cardápio",
            responses = {
                @ApiResponse(responseCode = "201", description = "Produto criado"),
                @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                @ApiResponse(responseCode = "404", description = "Unidade não encontrada"),
                @ApiResponse(responseCode = "403", description = "Sem permissão")
            })
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PostMapping
    public ResponseEntity<ProdutoResponse> criarProduto(@PathVariable Integer unidadeId,
            @Valid @RequestBody ProdutoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarProduto(unidadeId, request));
    }

    @Operation(summary = "Lista produtos da unidade com paginação",
            responses = {
                @ApiResponse(responseCode = "200", description = "Lista retornada"),
                @ApiResponse(responseCode = "404", description = "Unidade não encontrada")
            })
    @GetMapping
    public ResponseEntity<Page<ProdutoResponse>> listarProdutos(@PathVariable Integer unidadeId, Pageable pageable) {
        return ResponseEntity.ok(service.listarUnidades(unidadeId, pageable));
    }

    @Operation(summary = "Atualiza produto da unidade", description = "ADMIN e GERENTE podem atualizar")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(@PathVariable Integer unidadeId, @PathVariable Integer id,
            @Valid @RequestBody ProdutoRequest request) {
        return ResponseEntity.ok(service.atualizar(unidadeId, id, request));
    }

    @Operation(summary = "Remove produto da unidade", description = "Desativa o produto (soft delete)")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarProduto(@PathVariable Integer unidadeId, @PathVariable Integer id) {
        service.remover(unidadeId, id);
    }
}
