package com.lluanps.raizes_nordeste_api_uninter.unidade.controller;

import com.lluanps.raizes_nordeste_api_uninter.unidade.dto.UnidadeRequest;
import com.lluanps.raizes_nordeste_api_uninter.unidade.dto.UnidadeResponse;
import com.lluanps.raizes_nordeste_api_uninter.unidade.model.Unidade;
import com.lluanps.raizes_nordeste_api_uninter.unidade.service.UnidadeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/unidades")
public class UnidadeController {

    @Autowired
    private UnidadeService service;

    @PostMapping
    public ResponseEntity<UnidadeResponse> criarUnidade(@Valid @RequestBody UnidadeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarUnidade(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Unidade> buscarUnidadeById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findUnidadeById(id));
    }

    @GetMapping
    public ResponseEntity<Page<UnidadeResponse>> listarUnidades(Pageable pageable) {
        return ResponseEntity.ok(service.listarUnidades(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UnidadeResponse> atualizar(@PathVariable Integer id, @Valid @RequestBody UnidadeRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @PutMapping("/desativar/{id}")
    public ResponseEntity<UnidadeResponse> desativarUnidade(@PathVariable Integer id) {
        return ResponseEntity.ok(service.desativarUnidade(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarUnidade(@PathVariable Integer id) {
        service.deletarUnidade(id);
    }

}
