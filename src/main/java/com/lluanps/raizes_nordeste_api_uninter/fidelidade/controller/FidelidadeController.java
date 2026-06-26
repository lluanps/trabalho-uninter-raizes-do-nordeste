package com.lluanps.raizes_nordeste_api_uninter.fidelidade.controller;

import com.lluanps.raizes_nordeste_api_uninter.fidelidade.dto.ResgatePontosRequest;
import com.lluanps.raizes_nordeste_api_uninter.fidelidade.dto.ResgatePontosResponse;
import com.lluanps.raizes_nordeste_api_uninter.fidelidade.dto.SaldoPontosResponse;
import com.lluanps.raizes_nordeste_api_uninter.fidelidade.service.FidelidadeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fidelidade")
public class FidelidadeController {

    @Autowired
    private FidelidadeService service;

    @GetMapping("/saldo")
    public ResponseEntity<SaldoPontosResponse> consultarSaldo(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(service.getSaldo(userDetails.getUsername()));
    }

    @PostMapping("/consentimento")
    public ResponseEntity<Void> registrarConsentimento(@AuthenticationPrincipal UserDetails userDetails) {
        service.registrarConsentimento(userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/resgate")
    public ResponseEntity<ResgatePontosResponse> resgatar(@Valid @RequestBody ResgatePontosRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(service.resgatarPontos(userDetails.getUsername(), request));
    }

}
