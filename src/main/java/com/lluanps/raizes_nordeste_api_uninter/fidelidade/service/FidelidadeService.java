package com.lluanps.raizes_nordeste_api_uninter.fidelidade.service;

import com.lluanps.raizes_nordeste_api_uninter.enums.TipoMovimentacaoPontos;
import com.lluanps.raizes_nordeste_api_uninter.exceptions.BussinessException;
import com.lluanps.raizes_nordeste_api_uninter.fidelidade.dto.MovimentacaoPontosResponse;
import com.lluanps.raizes_nordeste_api_uninter.fidelidade.dto.ResgatePontosRequest;
import com.lluanps.raizes_nordeste_api_uninter.fidelidade.dto.ResgatePontosResponse;
import com.lluanps.raizes_nordeste_api_uninter.fidelidade.dto.SaldoPontosResponse;
import com.lluanps.raizes_nordeste_api_uninter.fidelidade.model.Fidelidade;
import com.lluanps.raizes_nordeste_api_uninter.fidelidade.model.MovimentacaoPontosFidelidade;
import com.lluanps.raizes_nordeste_api_uninter.fidelidade.repository.FidelidadeRepository;
import com.lluanps.raizes_nordeste_api_uninter.fidelidade.repository.MovimentacaoPontosRepository;
import com.lluanps.raizes_nordeste_api_uninter.pedido.model.Pedido;
import com.lluanps.raizes_nordeste_api_uninter.usuario.model.Usuario;
import com.lluanps.raizes_nordeste_api_uninter.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FidelidadeService {

    @Autowired
    private FidelidadeRepository repository;

    @Autowired
    private MovimentacaoPontosRepository movimentacaoPontosRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final BigDecimal VALOR_PONTO = new BigDecimal("0.01");

    public SaldoPontosResponse getSaldo(String emailUsuario) {
        Fidelidade programa = findOrCreatePrograma(emailUsuario);
        List<MovimentacaoPontosResponse> movimentacaoPontosResponses = movimentacaoPontosRepository
                .findByProgramaFidelidadeId(programa.getId())
                .stream()
                .map(this::toMovimentacaoResponse)
                .toList();

        return SaldoPontosResponse.builder()
                .usuarioId(programa.getUsuario().getId())
                .saldoPontos(programa.getSaldoPontos())
                .consentimentoAtivo(programa.isConsentimentoAtivo())
                .historicoMovimentacaoPontos(movimentacaoPontosResponses)
                .build();
    }

    @Transactional
    public void registrarConsentimento(String emailUsuario) {
        Fidelidade programa = findOrCreatePrograma(emailUsuario);
        programa.setConsentimentoAtivo(true);
        programa.setConsentimentoData(LocalDateTime.now());
        repository.save(programa);
    }

    @Transactional
    public ResgatePontosResponse resgatarPontos(String emailUsuario, ResgatePontosRequest request) {
        Fidelidade programa = findOrCreatePrograma(emailUsuario);

        if (!programa.isConsentimentoAtivo()) {
            throw new BussinessException("Usuário não está inscrito no programa de fidelidade");
        }

        if (programa.getSaldoPontos() < request.getPontos()) {
            throw new BussinessException("Saldo de pontos insuficiente. Disponível no momento: " + programa.getSaldoPontos()
                    + ", Solicitado: " + request.getPontos());
        }

        BigDecimal desconto = VALOR_PONTO.multiply(BigDecimal.valueOf(request.getPontos()));
        programa.setSaldoPontos(programa.getSaldoPontos() - request.getPontos());
        repository.save(programa);

        MovimentacaoPontosFidelidade mov = MovimentacaoPontosFidelidade.builder()
                .programaFidelidade(programa)
                .tipo(TipoMovimentacaoPontos.DEBITO)
                .pontos(request.getPontos())
                .descricao("Resgate de " + request.getPontos() + " pontos, O desconto sera de R$ " + desconto)
                .build();
        movimentacaoPontosRepository.save(mov);

        return ResgatePontosResponse.builder()
                .pontosResgatados(request.getPontos())
                .saldoRestante(programa.getSaldoPontos())
                .descontoAplicado(desconto)
                .mensagem("Resgate realizado com sucesso. Desconto de R$ " + desconto + " aplicado.")
                .build();
    }

    @Transactional
    public void creditarPontos(Integer usuarioId, BigDecimal valorPedido, Pedido pedido) {
        Fidelidade programa = repository.findByUsuarioId(usuarioId).orElse(null);

        if (programa == null || !programa.isConsentimentoAtivo()) {
            return;
        }

        int pontosCreditar = valorPedido.intValue();
        programa.setSaldoPontos(programa.getSaldoPontos() + pontosCreditar);
        repository.save(programa);

        MovimentacaoPontosFidelidade mov = MovimentacaoPontosFidelidade.builder()
                .programaFidelidade(programa)
                .pedido(pedido)
                .tipo(TipoMovimentacaoPontos.CREDITO)
                .pontos(pontosCreditar)
                .descricao("Créditos por pedido #" + pedido.getId() + " - R$ " + valorPedido)
                .build();

        movimentacaoPontosRepository.save(mov);
    }

    private Fidelidade findOrCreatePrograma(String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new BussinessException("Usuário não encontrado com o email: " + emailUsuario));

        return repository.findByUsuarioId(usuario.getId())
                .orElseGet(() -> {

                    Fidelidade novo = Fidelidade.builder()
                            .usuario(usuario)
                            .saldoPontos(0)
                            .consentimentoAtivo(false)
                            .build();
                    return repository.save(novo);
                });
    }

    private MovimentacaoPontosResponse toMovimentacaoResponse(MovimentacaoPontosFidelidade m) {
        return MovimentacaoPontosResponse.builder()
                .id(m.getId())
                .tipo(m.getTipo())
                .pontos(m.getPontos())
                .descricao(m.getDescricao())
                .pedidoId(m.getPedido() != null ? m.getPedido().getId() : null)
                .criadoEm(m.getCriadoEm())
                .build();
    }

}
