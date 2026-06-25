package com.lluanps.raizes_nordeste_api_uninter.usuario.controller;

import com.lluanps.raizes_nordeste_api_uninter.unidade.dto.UnidadeRequest;
import com.lluanps.raizes_nordeste_api_uninter.unidade.dto.UnidadeResponse;
import com.lluanps.raizes_nordeste_api_uninter.unidade.model.Unidade;
import com.lluanps.raizes_nordeste_api_uninter.usuario.dto.RegistroRequest;
import com.lluanps.raizes_nordeste_api_uninter.usuario.dto.UsuarioResponse;
import com.lluanps.raizes_nordeste_api_uninter.usuario.model.Usuario;
import com.lluanps.raizes_nordeste_api_uninter.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUnidadeById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findUsuarioById(id));
    }

    @GetMapping
    public ResponseEntity<Page<UsuarioResponse>> listarUsuario(Pageable pageable) {
        return ResponseEntity.ok(service.listarUsuarios(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizar(@PathVariable Integer id, @Valid @RequestBody RegistroRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarUsuario(@PathVariable Integer id) {
        service.deletarUsuario(id);
    }

}
