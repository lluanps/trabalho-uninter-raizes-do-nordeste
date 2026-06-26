package com.lluanps.raizes_nordeste_api_uninter.pagamento.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoRequest {

    @NotBlank(message = "É necessário uma forma de pagamento")
    private String metodoPagamento;

}
