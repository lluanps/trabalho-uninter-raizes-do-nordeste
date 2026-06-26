package com.lluanps.raizes_nordeste_api_uninter.pagamento.mock;

import com.lluanps.raizes_nordeste_api_uninter.exceptions.PaymentServiceUnavailableException;
import com.lluanps.raizes_nordeste_api_uninter.exceptions.PaymentTimeoutException;
import com.lluanps.raizes_nordeste_api_uninter.pagamento.service.PagamentoExternoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@Slf4j
@Service
public class PagamentoExternoServiceMock implements PagamentoExternoService {

    private static final Random RANDOM = new Random();

    @Override
    public String processarPagamento(Integer pedidoId, BigDecimal valor, String metodoPagamento) {
        log.info("Mock gateway: processando pagamento pedido={} valor={} metodo={}",
                pedidoId, valor, metodoPagamento);

        // Simula uma chance de 5% de indisponibilidade
        if (RANDOM.nextInt(100) < 5) {
            throw new PaymentServiceUnavailableException("Serviço de pagamento temporariamente indisponível. Tente novamente em instantes.");
        }

        int centavos = valor.multiply(BigDecimal.valueOf(100)).intValue();

        if (centavos >= 20000) {
            // Simula um timeout para valores >= R$ 200
            throw new PaymentTimeoutException("Serviço de pagamento não respondeu dentro do tempo limite.");
        }

        String status = centavos < 10000 ? "APROVADO" : "RECUSADO";
        String mensagem = "APROVADO".equals(status) ? "Pagamento aprovado com sucesso" : "Pagamento recusado: limite insuficiente";

        return String.format(
                "{\"gateway\":\"RaizesPayMock\",\"pedidoId\":%d,\"valor\":%.2f," +
                        "\"metodo\":\"%s\",\"status\":\"%s\",\"mensagem\":\"%s\"," +
                        "\"transacaoId\":\"TXN-%d\",\"processadoEm\":\"%s\"}",
                pedidoId, valor, metodoPagamento, status, mensagem,
                System.currentTimeMillis(), LocalDateTime.now());
    }

}
