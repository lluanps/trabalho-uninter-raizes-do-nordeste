package com.lluanps.raizes_nordeste_api_uninter.unidade.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnidadeRequest {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    private String endereco;

    @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
    private String telefone;

}
