package com.lluanps.raizes_nordeste_api_uninter.fidelidade.controller;

import com.lluanps.raizes_nordeste_api_uninter.fidelidade.dto.ResgatePontosRequest;
import com.lluanps.raizes_nordeste_api_uninter.fidelidade.dto.ResgatePontosResponse;
import com.lluanps.raizes_nordeste_api_uninter.fidelidade.dto.SaldoPontosResponse;
import com.lluanps.raizes_nordeste_api_uninter.fidelidade.service.FidelidadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Fidelidade", description = "Programa de pontos e resgates")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/fidelidade")
public class FidelidadeController {

    @Autowired
    private FidelidadeService service;

    @Operation(summary = "Consulta saldo de pontos do usuário autenticado",
            description = "Retorna saldo atual e histórico de movimentações",
            responses = {
                @ApiResponse(responseCode = "200", description = "Saldo retornado")
            })
    @GetMapping("/saldo")
    public ResponseEntity<SaldoPontosResponse> consultarSaldo(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(service.getSaldo(userDetails.getUsername()));
    }

    @Operation(summary = "Registra consentimento no programa de fidelidade",
            description = "O usuário precisa dar consentimento (LGPD) antes de acumular ou resgatar pontos",
            responses = {
                @ApiResponse(responseCode = "204", description = "Consentimento registrado")
            })
    @PostMapping("/consentimento")
    public ResponseEntity<Void> registrarConsentimento(@AuthenticationPrincipal UserDetails userDetails) {
        service.registrarConsentimento(userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Resgata pontos do programa de fidelidade",
            description = "Cada ponto equivale a R$0,01 de desconto. Requer consentimento ativo.",
            responses = {
                @ApiResponse(responseCode = "200", description = "Resgate realizado"),
                @ApiResponse(responseCode = "400", description = "Saldo insuficiente ou consentimento não dado"),
                @ApiResponse(responseCode = "409", description = "Usuário não inscrito no programa")
            })
    @PostMapping("/resgate")
    public ResponseEntity<ResgatePontosResponse> resgatar(
            @Valid @RequestBody ResgatePontosRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(service.resgatarPontos(userDetails.getUsername(), request));
    }

}
