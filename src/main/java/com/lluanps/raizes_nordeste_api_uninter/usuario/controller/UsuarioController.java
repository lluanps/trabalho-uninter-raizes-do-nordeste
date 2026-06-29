package com.lluanps.raizes_nordeste_api_uninter.usuario.controller;

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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUnidadeById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findUsuarioById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UsuarioResponse>> listarUsuario(Pageable pageable) {
        return ResponseEntity.ok(service.listarUsuarios(pageable));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizar(@PathVariable Integer id, @Valid @RequestBody RegistroRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarUsuario(@PathVariable Integer id) {
        service.deletarUsuario(id);
    }

}
