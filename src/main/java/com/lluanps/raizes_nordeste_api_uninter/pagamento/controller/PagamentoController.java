package com.lluanps.raizes_nordeste_api_uninter.pagamento.controller;

import com.lluanps.raizes_nordeste_api_uninter.pagamento.dto.PagamentoRequest;
import com.lluanps.raizes_nordeste_api_uninter.pagamento.dto.PagamentoResponse;
import com.lluanps.raizes_nordeste_api_uninter.pagamento.mock.PagamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos/{pedidoId}/pagamento")
public class PagamentoController {

    @Autowired
    private PagamentoService service;

    @PostMapping
    public ResponseEntity<PagamentoResponse> processar(@PathVariable Integer pedidoId, @Valid @RequestBody PagamentoRequest request,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(service.processar(pedidoId, request, userDetails.getUsername()));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'ATENDENTE')")
    @GetMapping
    public ResponseEntity<PagamentoResponse> consultar(@PathVariable Integer pedidoId) {
        return ResponseEntity.ok(service.consultar(pedidoId));
    }

}
