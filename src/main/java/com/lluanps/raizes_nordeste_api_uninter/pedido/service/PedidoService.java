package com.lluanps.raizes_nordeste_api_uninter.pedido.service;

import com.lluanps.raizes_nordeste_api_uninter.enums.CanalPedido;
import com.lluanps.raizes_nordeste_api_uninter.enums.StatusPedido;
import com.lluanps.raizes_nordeste_api_uninter.enums.TipoMovimentacaoEstoque;
import com.lluanps.raizes_nordeste_api_uninter.estoque.model.Estoque;
import com.lluanps.raizes_nordeste_api_uninter.estoque.model.MovimentacaoEstoque;
import com.lluanps.raizes_nordeste_api_uninter.estoque.repository.EstoqueRepository;
import com.lluanps.raizes_nordeste_api_uninter.estoque.repository.MovimentacaoEstoqueRepository;
import com.lluanps.raizes_nordeste_api_uninter.exceptions.BussinessException;
import com.lluanps.raizes_nordeste_api_uninter.exceptions.NotFoundException;
import com.lluanps.raizes_nordeste_api_uninter.fidelidade.service.FidelidadeService;
import com.lluanps.raizes_nordeste_api_uninter.pedido.dto.*;
import com.lluanps.raizes_nordeste_api_uninter.pedido.model.HistoricoStatusPedido;
import com.lluanps.raizes_nordeste_api_uninter.pedido.model.ItemPedido;
import com.lluanps.raizes_nordeste_api_uninter.pedido.model.Pedido;
import com.lluanps.raizes_nordeste_api_uninter.pedido.repository.HistoricoRepository;
import com.lluanps.raizes_nordeste_api_uninter.pedido.repository.ItemPedidoRepository;
import com.lluanps.raizes_nordeste_api_uninter.pedido.repository.PedidoRepository;
import com.lluanps.raizes_nordeste_api_uninter.produto.model.Produto;
import com.lluanps.raizes_nordeste_api_uninter.produto.repository.ProdutoRepository;
import com.lluanps.raizes_nordeste_api_uninter.unidade.model.Unidade;
import com.lluanps.raizes_nordeste_api_uninter.unidade.repository.UnidadeRepository;
import com.lluanps.raizes_nordeste_api_uninter.usuario.model.Usuario;
import com.lluanps.raizes_nordeste_api_uninter.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    @Autowired
    private HistoricoRepository historicoRepository;

    @Autowired
    private UnidadeRepository unidadeRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private FidelidadeService fidelidadeService;

    private static final Map<StatusPedido, Set<StatusPedido>> TRANSICOES_VALIDAS = Map.of(
            StatusPedido.PENDENTE, Set.of(StatusPedido.EM_PREPARO, StatusPedido.CANCELADO),
            StatusPedido.EM_PREPARO, Set.of(StatusPedido.PRONTO, StatusPedido.CANCELADO),
            StatusPedido.PRONTO, Set.of(StatusPedido.ENTREGUE),
            StatusPedido.ENTREGUE, Set.of(),
            StatusPedido.CANCELADO, Set.of()
    );

    public PedidoResponse registraNovoPedido(CriaPedidoRequest request, String username) {
        Usuario usuario = findUsuarioByEmail(username);
        Unidade unidade = findUnidadeById(request.getUnidadeId());

        List<ItemPedido> itens = montarItensDoPedido(request, unidade);
        BigDecimal valorTotal = itens.stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Pedido pedido = salvarPedidoComItens(request, usuario, unidade, itens, valorTotal);

        registrarHistoricoInicial(pedido, usuario);

        return toResponse(pedido, itens);
    }

    private List<ItemPedido> montarItensDoPedido(CriaPedidoRequest request, Unidade unidade) {
        List<ItemPedido> itens = new ArrayList<>();

        for (ItemPedidoRequest itemPedidoRequest : request.getItens()) {
            Produto produto = produtoRepository.findById(itemPedidoRequest.getProdutoId())
                    .orElseThrow(() -> new BussinessException("Produto não encontrado com id: " + itemPedidoRequest.getProdutoId()));

            Estoque estoque = estoqueRepository.findByProdutoIdAndUnidadeId(produto.getId(), unidade.getId())
                    .orElseThrow(() -> new BussinessException("Produto " + produto.getNome() + " não disponível nesta unidade"));

            if (estoque.getQuantidade() < itemPedidoRequest.getQuantidade()) {
                throw new BussinessException("Estoque insuficiente para " + produto.getNome()
                        + ". Disponível: " + estoque.getQuantidade()
                        + ", Solicitado: " + itemPedidoRequest.getQuantidade());
            }

            BigDecimal subtotal = produto.getPreco().multiply(BigDecimal.valueOf(itemPedidoRequest.getQuantidade()));

            itens.add(ItemPedido.builder()
                    .produto(produto)
                    .quantidade(itemPedidoRequest.getQuantidade())
                    .precoUnitario(produto.getPreco())
                    .subtotal(subtotal)
                    .build());
        }

        return itens;
    }

    private Pedido salvarPedidoComItens(CriaPedidoRequest request, Usuario usuario, Unidade unidade,List<ItemPedido> itens, BigDecimal valorTotal) {
        Pedido pedido = Pedido.builder()
                .usuario(usuario)
                .unidade(unidade)
                .status(StatusPedido.PENDENTE)
                .canal(request.getCanalPedido())
                .valorTotal(valorTotal)
                .desconto(BigDecimal.ZERO)
                .pontosUtilizados(0)
                .build();

        pedido = repository.save(pedido);

        for (int i = 0; i < itens.size(); i++) {
            ItemPedido itemSalvo = itens.get(i);
            itemSalvo.setPedido(pedido);
            itemPedidoRepository.save(itemSalvo);

            ItemPedidoRequest itemOriginal = request.getItens().get(i);
            Estoque estoque = estoqueRepository.findByProdutoIdAndUnidadeId(itemOriginal.getProdutoId(), unidade.getId())
                    .orElseThrow();

            estoque.setQuantidade(estoque.getQuantidade() - itemOriginal.getQuantidade());
            estoqueRepository.save(estoque);

            movimentacaoEstoqueRepository.save(MovimentacaoEstoque.builder()
                    .estoque(estoque)
                    .usuario(usuario)
                    .tipo(TipoMovimentacaoEstoque.SAIDA)
                    .quantidade(itemOriginal.getQuantidade())
                    .observacao("Baixa de estoque pelo pedido: " + pedido.getId())
                    .build());
        }

        return pedido;
    }

    private void registrarHistoricoInicial(Pedido pedido, Usuario usuario) {
        historicoRepository.save(HistoricoStatusPedido.builder()
                .pedido(pedido)
                .usuario(usuario)
                .statusAnterior(null)
                .statusNovo(StatusPedido.PENDENTE)
                .build());
    }

    @Transactional
    public PedidoResponse atualizarStatus(Integer pedidoId, StatusPedidoRequest request, String emailUsuario) {
        Pedido pedido = findPedidoById(pedidoId);
        Usuario usuario = findUsuarioByEmail(emailUsuario);
        StatusPedido statusAtual = pedido.getStatus();
        StatusPedido novoStatus = request.getNovoStatus();

        Set<StatusPedido> permitidos = TRANSICOES_VALIDAS.getOrDefault(statusAtual, Set.of());
        if (!permitidos.contains(novoStatus)) {
            throw new BussinessException("Transição inválida: "+ statusAtual + " → " + novoStatus + ". Transições permitidas: " + permitidos);
        }

        if (novoStatus == StatusPedido.CANCELADO) {
            restaurarEstoque(pedido, usuario);
        }

        HistoricoStatusPedido historico = HistoricoStatusPedido.builder()
                .pedido(pedido)
                .usuario(usuario)
                .statusAnterior(statusAtual)
                .statusNovo(novoStatus)
                .build();
        historicoRepository.save(historico);

        pedido.setStatus(novoStatus);
        pedido = pedidoRepository.save(pedido);

        if (novoStatus == StatusPedido.ENTREGUE) {
            fidelidadeService.creditarPontos( pedido.getUsuario().getId(), pedido.getValorTotal(), pedido);
        }

        List<ItemPedido> itens = itemPedidoRepository.findByPedidoId(pedidoId);
        return toResponse(pedido, itens);
    }

    private void restaurarEstoque(Pedido pedido, Usuario usuario) {
        List<ItemPedido> itens = itemPedidoRepository.findByPedidoId(pedido.getId());
        for (ItemPedido item : itens) {
            estoqueRepository.findByProdutoIdAndUnidadeId(
                            item.getProduto().getId(), pedido.getUnidade().getId())
                    .ifPresent(estoque -> {
                        estoque.setQuantidade(estoque.getQuantidade() + item.getQuantidade());
                        estoqueRepository.save(estoque);

                        MovimentacaoEstoque mov = MovimentacaoEstoque.builder()
                                .estoque(estoque)
                                .usuario(usuario)
                                .tipo(TipoMovimentacaoEstoque.ENTRADA)
                                .quantidade(item.getQuantidade())
                                .observacao("Cancelamento pedido #" + pedido.getId())
                                .build();
                        movimentacaoEstoqueRepository.save(mov);
                    });
        }
    }


    private Pedido findPedidoById(Integer id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido não encontrado: " + id));
    }

    private Usuario findUsuarioByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado: " + email));
    }

    private Unidade findUnidadeById(Integer id) {
        return unidadeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Unidade não encontrada: " + id));
    }

    public PedidoResponse toResponse(Pedido pedido, List<ItemPedido> itens) {
        List<ItemPedidoResponse> itensResponse = itens.stream()
                .map(item -> ItemPedidoResponse.builder()
                        .id(item.getId())
                        .produtoId(item.getProduto().getId())
                        .produtoNome(item.getProduto().getNome())
                        .quantidade(item.getQuantidade())
                        .precoUnitario(item.getPrecoUnitario())
                        .subtotal(item.getSubtotal())
                        .build())
                .toList();

        return PedidoResponse.builder()
                .id(pedido.getId())
                .usuarioId(pedido.getUsuario().getId())
                .usuarioNome(pedido.getUsuario().getNome())
                .unidadeId(pedido.getUnidade().getId())
                .unidadeNome(pedido.getUnidade().getNome())
                .status(pedido.getStatus())
                .canal(pedido.getCanal())
                .valorTotal(pedido.getValorTotal())
                .desconto(pedido.getDesconto())
                .pontosUtilizados(pedido.getPontosUtilizados())
                .itens(itensResponse)
                .criadoEm(pedido.getCriadoEm())
                .atualizadoEm(pedido.getAtualizadoEm())
                .build();
    }

    public Page<PedidoResponse> listar(CanalPedido canal, StatusPedido status, Integer usuarioId, Pageable pageable) {
        Page<Pedido> pedidos;
        if (canal != null) {
            pedidos = pedidoRepository.findByCanal(canal, pageable);
        } else if (status != null) {
            pedidos = pedidoRepository.findByStatus(status, pageable);
        } else if (usuarioId != null) {
            pedidos = pedidoRepository.findByUsuarioId(usuarioId, pageable);
        } else {
            pedidos = pedidoRepository.findAll(pageable);
        }

        return pedidos.map(p -> {
            List<ItemPedido> itens = itemPedidoRepository.findByPedidoId(p.getId());
            return toResponse(p, itens);
        });
    }

    public PedidoResponse buscaPedidoPorId(Integer id) {
        Pedido pedido = findPedidoById(id);
        List<ItemPedido> itens = itemPedidoRepository.findByPedidoId(id);
        return toResponse(pedido, itens);
    }
}
