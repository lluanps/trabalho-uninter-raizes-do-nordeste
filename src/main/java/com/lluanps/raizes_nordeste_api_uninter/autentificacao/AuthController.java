package com.lluanps.raizes_nordeste_api_uninter.autentificacao;

import com.lluanps.raizes_nordeste_api_uninter.security.dto.LoginRequest;
import com.lluanps.raizes_nordeste_api_uninter.security.dto.TokenResponse;
import com.lluanps.raizes_nordeste_api_uninter.usuario.dto.RegistroRequest;
import com.lluanps.raizes_nordeste_api_uninter.usuario.dto.UsuarioResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Autenticação", description = "Login e registro de usuários")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Realiza login", description = "Autentica o usuário e retorna um token JWT",
            responses = {
                @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
                @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
                @ApiResponse(responseCode = "400", description = "Dados inválidos")
            })
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(summary = "Registra novo usuário", description = "Cria uma conta com perfil CLIENTE",
            responses = {
                @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                @ApiResponse(responseCode = "409", description = "Email ou CPF já cadastrado")
            })
    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponse> registrarUsuario(@Valid @RequestBody RegistroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrar(request));
    }

}
