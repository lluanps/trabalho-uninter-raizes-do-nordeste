package com.lluanps.raizes_nordeste_api_uninter.estoque.controller;

import com.lluanps.raizes_nordeste_api_uninter.estoque.dto.EstoqueRequest;
import com.lluanps.raizes_nordeste_api_uninter.estoque.dto.EstoqueResponse;
import com.lluanps.raizes_nordeste_api_uninter.estoque.service.EstoqueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/unidades/{id}/estoque")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PostMapping
    public ResponseEntity<EstoqueResponse> registraMovimentacaoEstoque(@PathVariable("id") Integer unidadeId, @Valid @RequestBody EstoqueRequest request,
                                                                       @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(estoqueService.registraMovimentacaoEstoque(unidadeId, request, userDetails.getUsername()));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COZINHA')")
    @GetMapping("/{produtoId}")
    public ResponseEntity<EstoqueResponse> consultarEstoque(@PathVariable("id") Integer unidadeId, @PathVariable("produtoId") Integer produtoId) {
        return ResponseEntity.ok(estoqueService.consultarEstoque(unidadeId, produtoId));
    }

}
