package com.lluanps.raizes_nordeste_api_uninter.autentificacao;

import com.lluanps.raizes_nordeste_api_uninter.security.dto.LoginRequest;
import com.lluanps.raizes_nordeste_api_uninter.security.dto.TokenResponse;
import com.lluanps.raizes_nordeste_api_uninter.usuario.dto.RegistroRequest;
import com.lluanps.raizes_nordeste_api_uninter.usuario.dto.UsuarioResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponse> registrarUsuario(@Valid @RequestBody RegistroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrar(request));
    }

}
