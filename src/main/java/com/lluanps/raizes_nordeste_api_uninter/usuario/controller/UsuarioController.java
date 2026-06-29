package com.lluanps.raizes_nordeste_api_uninter.usuario.controller;

import com.lluanps.raizes_nordeste_api_uninter.usuario.dto.RegistroRequest;
import com.lluanps.raizes_nordeste_api_uninter.usuario.dto.UsuarioResponse;
import com.lluanps.raizes_nordeste_api_uninter.usuario.model.Usuario;
import com.lluanps.raizes_nordeste_api_uninter.usuario.service.UsuarioService;
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

@Tag(name = "Usuários", description = "Gestão de usuários — somente ADMIN")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @Operation(summary = "Busca usuário por Id",
            responses = {
                @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
                @ApiResponse(responseCode = "403", description = "Sem permissão"),
                @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuarioById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findUsuarioById(id));
    }

    @Operation(summary = "Lista todos os usuários com paginação")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UsuarioResponse>> listarUsuario(Pageable pageable) {
        return ResponseEntity.ok(service.listarUsuarios(pageable));
    }

    @Operation(summary = "Atualiza dados de um usuário",
            responses = {
                @ApiResponse(responseCode = "200", description = "Usuário atualizado"),
                @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
                @ApiResponse(responseCode = "403", description = "Sem permissão")
            })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizar(@PathVariable Integer id, @Valid @RequestBody RegistroRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @Operation(summary = "Deleta um usuário",
            responses = {
                @ApiResponse(responseCode = "204", description = "Usuário removido"),
                @ApiResponse(responseCode = "403", description = "Sem permissão"),
                @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarUsuario(@PathVariable Integer id) {
        service.deletarUsuario(id);
    }

}
