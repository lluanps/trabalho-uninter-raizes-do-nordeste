package com.lluanps.raizes_nordeste_api_uninter.fidelidade.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResgatePontosRequest {

    @NotNull(message = "A Quantidade de pontos é obrigatória")
    @Min(value = 1, message = "Quantidade de pontos deve ser maior do que 1")
    private Integer pontos;
}
