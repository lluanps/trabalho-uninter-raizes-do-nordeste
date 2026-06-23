package com.lluanps.raizes_nordeste_api_uninter.usuario.dto;

import com.lluanps.raizes_nordeste_api_uninter.enums.Perfil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {

    private Integer id;
    private String nome;
    private String email;
    private Perfil perfil;
    private boolean ativo;
    private LocalDateTime criadoEm;
}
