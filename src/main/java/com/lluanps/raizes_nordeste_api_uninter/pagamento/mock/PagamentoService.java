package com.lluanps.raizes_nordeste_api_uninter.pagamento.mock;

import com.lluanps.raizes_nordeste_api_uninter.enums.StatusPagamento;
import com.lluanps.raizes_nordeste_api_uninter.exceptions.BussinessException;
import com.lluanps.raizes_nordeste_api_uninter.exceptions.PaymentServiceUnavailableException;
import com.lluanps.raizes_nordeste_api_uninter.exceptions.PaymentTimeoutException;
import com.lluanps.raizes_nordeste_api_uninter.logs.model.LogAuditoria;
import com.lluanps.raizes_nordeste_api_uninter.logs.repository.LogAuditoriaRepository;
import com.lluanps.raizes_nordeste_api_uninter.pagamento.dto.PagamentoRequest;
import com.lluanps.raizes_nordeste_api_uninter.pagamento.dto.PagamentoResponse;
import com.lluanps.raizes_nordeste_api_uninter.pagamento.model.Pagamento;
import com.lluanps.raizes_nordeste_api_uninter.pagamento.repository.PagamentoRepository;
import com.lluanps.raizes_nordeste_api_uninter.pagamento.service.PagamentoExternoService;
import com.lluanps.raizes_nordeste_api_uninter.pedido.model.Pedido;
import com.lluanps.raizes_nordeste_api_uninter.pedido.repository.PedidoRepository;
import com.lluanps.raizes_nordeste_api_uninter.usuario.model.Usuario;
import com.lluanps.raizes_nordeste_api_uninter.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository repository;

    @Autowired
    private PagamentoExternoService pagamentoExternoService;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LogAuditoriaRepository logAuditoriaRepository;

    public PagamentoResponse processar(Integer pedidoId, PagamentoRequest request,
                                       String emailUsuario) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new BussinessException("Pedido nao encontrado com id: " + pedidoId));

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new BussinessException("Usuário não encontrado: " + emailUsuario));

        String payloadReq = String.format(
                "{\"pedidoId\":%d,\"valor\":%.2f,\"metodo\":\"%s\"}",
                pedidoId, pedido.getValorTotal(), request.getMetodoPagamento());

        Pagamento pagamento = Pagamento.builder()
                .pedido(pedido)
                .valor(pedido.getValorTotal())
                .metodoPagamento(request.getMetodoPagamento())
                .status(StatusPagamento.PENDENTE)
                .payloadRequisicao(payloadReq)
                .build();

        return getPagamentoResponse(pedidoId, request, pedido, pagamento, usuario);
    }

    private PagamentoResponse getPagamentoResponse(Integer pedidoId, PagamentoRequest request, Pedido pedido, Pagamento pagamento, Usuario usuario) {
        try {
            String payloadResp = pagamentoExternoService.processarPagamento(pedidoId, pedido.getValorTotal(), request.getMetodoPagamento());

            StatusPagamento status = payloadResp.contains("\"APROVADO\"") ? StatusPagamento.APROVADO : StatusPagamento.RECUSADO;

            pagamento.setStatus(status);
            pagamento.setPayloadResposta(payloadResp);
            pagamento = repository.save(pagamento);

            registrarAuditoria(usuario, "PAGAMENTO_" + status,"Pagamento", pedidoId, payloadResp);

            return toResponse(pagamento);

        } catch (PaymentTimeoutException e) {
            pagamento.setStatus(StatusPagamento.ERRO);
            pagamento.setPayloadResposta("{\"erro\":\"timeout\",\"mensagem\":\"" + e.getMessage() + "\"}");
            repository.save(pagamento);
            registrarAuditoria(usuario, "PAGAMENTO_TIMEOUT", "Pagamento", pedidoId, e.getMessage());
            throw e;

        } catch (PaymentServiceUnavailableException e) {
            pagamento.setStatus(StatusPagamento.ERRO);
            pagamento.setPayloadResposta("{\"erro\":\"indisponivel\",\"mensagem\":\"" + e.getMessage() + "\"}");
            repository.save(pagamento);
            registrarAuditoria(usuario, "PAGAMENTO_INDISPONIVEL", "Pagamento", pedidoId, e.getMessage());
            throw e;
        }
    }

    public PagamentoResponse consultar(Integer pedidoId) {
        Pagamento pagamento = repository.findTopByPedidoIdOrderByCriadoEmDesc(pedidoId)
                .orElseThrow(() -> new BussinessException("Pagamento não encontrado para o pedido " + pedidoId));

        return toResponse(pagamento);
    }

    private void registrarAuditoria(Usuario usuario, String acao, String entidade, Integer entidadeId, String detalhes) {
        LogAuditoria log = LogAuditoria.builder()
                .usuario(usuario)
                .acao(acao)
                .entidade(entidade)
                .entidadeId(entidadeId)
                .detalhes(detalhes)
                .build();
        logAuditoriaRepository.save(log);
    }

    private PagamentoResponse toResponse(Pagamento p) {
        return PagamentoResponse.builder()
                .id(p.getId())
                .pedidoId(p.getPedido().getId())
                .valor(p.getValor())
                .metodoPagamento(p.getMetodoPagamento())
                .status(p.getStatus())
                .payloadRequisicao(p.getPayloadRequisicao())
                .payloadResposta(p.getPayloadResposta())
                .criadoEm(p.getCriadoEm())
                .atualizadoEm(p.getAtualizadoEm())
                .build();
    }
}
