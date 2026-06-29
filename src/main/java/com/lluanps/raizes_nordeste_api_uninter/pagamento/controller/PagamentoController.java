package com.lluanps.raizes_nordeste_api_uninter.pagamento.controller;

import com.lluanps.raizes_nordeste_api_uninter.pagamento.dto.PagamentoRequest;
import com.lluanps.raizes_nordeste_api_uninter.pagamento.dto.PagamentoResponse;
import com.lluanps.raizes_nordeste_api_uninter.pagamento.mock.PagamentoService;
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

@Tag(name = "Pagamentos", description = "Simulação de pagamento via gateway (mock)")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/pedidos/{pedidoId}/pagamento")
public class PagamentoController {

    @Autowired
    private PagamentoService service;

    @Operation(summary = "Processa o pagamento de um pedido",
            description = "Envia o pagamento para o gateway mock. Valores que são abaixo de R$100 são aprovados, acima disso, é  recusados. Também existe 5% de chance de indisponibilidade e timeout para valores acima de R$200",
            responses = {
                @ApiResponse(responseCode = "200", description = "Pagamento processado (aprovado ou recusado)"),
                @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
                @ApiResponse(responseCode = "503", description = "Gateway de pagamento indisponível"),
                @ApiResponse(responseCode = "504", description = "Timeout no gateway de pagamento")
            })
    @PostMapping
    public ResponseEntity<PagamentoResponse> processar(@PathVariable Integer pedidoId, @Valid @RequestBody PagamentoRequest request,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(service.processar(pedidoId, request, userDetails.getUsername()));
    }

    @Operation(summary = "Consulta o pagamento do pedido",
            description = "Retorna o último pagamento registrado para o pedido",
            responses = {
                @ApiResponse(responseCode = "200", description = "Pagamento encontrado"),
                @ApiResponse(responseCode = "404", description = "Nenhum pagamento encontrado para o pedido"),
                @ApiResponse(responseCode = "403", description = "Sem permissão")
            })
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'ATENDENTE')")
    @GetMapping
    public ResponseEntity<PagamentoResponse> consultar(@PathVariable Integer pedidoId) {
        return ResponseEntity.ok(service.consultar(pedidoId));
    }

}
