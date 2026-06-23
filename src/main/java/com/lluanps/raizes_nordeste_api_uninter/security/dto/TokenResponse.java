package com.lluanps.raizes_nordeste_api_uninter.security.dto;

import com.lluanps.raizes_nordeste_api_uninter.usuario.dto.UsuarioResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {

    private String token;
    private String tipo;
    private long expiracao;
    private UsuarioResponse usuario;
}
