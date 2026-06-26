package com.lluanps.raizes_nordeste_api_uninter.pagamento.service;

// interface usada para simular um serviço externo de pagamento.
public interface PagamentoExternoService {

    String processarPagamento(Integer pedidoId, java.math.BigDecimal valor, String metodoPagamento);
}