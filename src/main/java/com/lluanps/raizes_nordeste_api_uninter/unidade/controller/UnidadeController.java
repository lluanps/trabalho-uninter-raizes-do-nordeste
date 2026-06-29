package com.lluanps.raizes_nordeste_api_uninter.unidade.controller;

import com.lluanps.raizes_nordeste_api_uninter.unidade.dto.UnidadeRequest;
import com.lluanps.raizes_nordeste_api_uninter.unidade.dto.UnidadeResponse;
import com.lluanps.raizes_nordeste_api_uninter.unidade.model.Unidade;
import com.lluanps.raizes_nordeste_api_uninter.unidade.service.UnidadeService;
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

@Tag(name = "Unidades", description = "Gestão das unidades da rede")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/unidades")
public class UnidadeController {

    @Autowired
    private UnidadeService service;

    @Operation(summary = "Cria uma nova unidade", description = "Somente ADMIN pode cadastrar novas unidades",
            responses = {
                @ApiResponse(responseCode = "201", description = "Unidade criada"),
                @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                @ApiResponse(responseCode = "403", description = "Sem permissão")
            })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UnidadeResponse> criarUnidade(@Valid @RequestBody UnidadeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarUnidade(request));
    }

    @Operation(summary = "Busca unidade por ID",
            responses = {
                @ApiResponse(responseCode = "200", description = "Unidade encontrada"),
                @ApiResponse(responseCode = "404", description = "Unidade não encontrada")
            })
    @GetMapping("/{id}")
    public ResponseEntity<Unidade> buscarUnidadeById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findUnidadeById(id));
    }

    @Operation(summary = "Lista todas as unidades com paginação")
    @GetMapping
    public ResponseEntity<Page<UnidadeResponse>> listarUnidades(Pageable pageable) {
        return ResponseEntity.ok(service.listarUnidades(pageable));
    }

    @Operation(summary = "Atualiza dados de uma unidade", description = "ADMIN e GERENTE podem atualizar",
            responses = {
                @ApiResponse(responseCode = "200", description = "Unidade atualizada"),
                @ApiResponse(responseCode = "404", description = "Unidade não encontrada"),
                @ApiResponse(responseCode = "403", description = "Sem permissão")
            })
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PutMapping("/{id}")
    public ResponseEntity<UnidadeResponse> atualizar(@PathVariable Integer id, @Valid @RequestBody UnidadeRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @Operation(summary = "Desativa uma unidade", description = "ADMIN e GERENTE podem desativar")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PutMapping("/desativar/{id}")
    public ResponseEntity<UnidadeResponse> desativarUnidade(@PathVariable Integer id) {
        return ResponseEntity.ok(service.desativarUnidade(id));
    }

    @Operation(summary = "Remove uma unidade", description = "Somente ADMIN pode remover unidades",
            responses = {
                @ApiResponse(responseCode = "204", description = "Unidade removida"),
                @ApiResponse(responseCode = "403", description = "Sem permissão"),
                @ApiResponse(responseCode = "404", description = "Unidade não encontrada")
            })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarUnidade(@PathVariable Integer id) {
        service.deletarUnidade(id);
    }

}
