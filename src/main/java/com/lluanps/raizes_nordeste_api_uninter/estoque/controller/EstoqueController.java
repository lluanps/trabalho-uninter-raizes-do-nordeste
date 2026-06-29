package com.lluanps.raizes_nordeste_api_uninter.estoque.controller;

import com.lluanps.raizes_nordeste_api_uninter.estoque.dto.EstoqueRequest;
import com.lluanps.raizes_nordeste_api_uninter.estoque.dto.EstoqueResponse;
import com.lluanps.raizes_nordeste_api_uninter.estoque.service.EstoqueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Estoque", description = "Controle de estoque por unidade")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/unidades/{id}/estoque")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    @Operation(summary = "Registra movimentação de entrada e saída de estoque",
            description = "ADMIN e GERENTE movimentam o estoque. Use ENTRADA ou SAIDA no campo 'TipoMovimentacaoEstoque' no body.",
            responses = {
                @ApiResponse(responseCode = "200", description = "Movimentação registrada"),
                @ApiResponse(responseCode = "400", description = "Dados inválidos ou estoque insuficiente"),
                @ApiResponse(responseCode = "404", description = "Produto ou unidade não encontrado"),
                @ApiResponse(responseCode = "403", description = "Sem permissão")
            })
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PostMapping
    public ResponseEntity<EstoqueResponse> registraMovimentacaoEstoque(@PathVariable("id") Integer unidadeId, @Valid @RequestBody EstoqueRequest request,
                                                                       @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(estoqueService.registraMovimentacaoEstoque(unidadeId, request, userDetails.getUsername()));
    }

    @Operation(summary = "Consulta o saldo de estoque de um produto da unidade",
            description = "ADMIN, GERENTE e COZINHA podem consultar",
            responses = {
                @ApiResponse(responseCode = "200", description = "Saldo retornado"),
                @ApiResponse(responseCode = "404", description = "Produto não encontrado na unidade")
            })
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COZINHA')")
    @GetMapping("/{produtoId}")
    public ResponseEntity<EstoqueResponse> consultarEstoque(@PathVariable("id") Integer unidadeId, @PathVariable("produtoId") Integer produtoId) {
        return ResponseEntity.ok(estoqueService.consultarEstoque(unidadeId, produtoId));
    }

}
