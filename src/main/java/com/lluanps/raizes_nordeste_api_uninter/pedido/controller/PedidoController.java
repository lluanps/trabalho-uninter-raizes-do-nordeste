package com.lluanps.raizes_nordeste_api_uninter.pedido.controller;

import com.lluanps.raizes_nordeste_api_uninter.enums.CanalPedido;
import com.lluanps.raizes_nordeste_api_uninter.enums.StatusPedido;
import com.lluanps.raizes_nordeste_api_uninter.pedido.dto.CriaPedidoRequest;
import com.lluanps.raizes_nordeste_api_uninter.pedido.dto.PedidoResponse;
import com.lluanps.raizes_nordeste_api_uninter.pedido.dto.StatusPedidoRequest;
import com.lluanps.raizes_nordeste_api_uninter.pedido.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Pedidos", description = "Criação, consulta e atualização de status dos pedidos")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService service;

    @Operation(summary = "Registra um novo pedido",
            description = "Valida estoque, baixa quantidade e registra o pedido. O campo 'canalPedido' é obrigatório (APP, TOTEM, BALCAO, PICKUP, WEB).",
            responses = {
                @ApiResponse(responseCode = "201", description = "Pedido criado"),
                @ApiResponse(responseCode = "400", description = "Dados inválidos ou canalPedido ausente"),
                @ApiResponse(responseCode = "404", description = "Produto ou unidade não encontrado"),
                @ApiResponse(responseCode = "409", description = "Estoque insuficiente")
            })
    @PostMapping
    public ResponseEntity<PedidoResponse> registraNovoPedido(@Valid @RequestBody CriaPedidoRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registraNovoPedido(request, userDetails.getUsername()));
    }

    @Operation(summary = "Lista pedidos com filtros",
            description = "Filtra por canal (APP, TOTEM, BALCAO, PICKUP, WEB), status e usuário. Paginado.",
            responses = {
                @ApiResponse(responseCode = "200", description = "Lista retornada"),
                @ApiResponse(responseCode = "403", description = "Sem permissão")
            })
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'ATENDENTE', 'COZINHA')")
    @GetMapping
    public ResponseEntity<Page<PedidoResponse>> listar(
            @Parameter(description = "Filtrar por canal do pedido") @RequestParam(required = false) CanalPedido canalPedido,
            @Parameter(description = "Filtrar por status") @RequestParam(required = false) StatusPedido status,
            @Parameter(description = "Filtrar por ID do usuário") @RequestParam(required = false) Integer usuarioId,
            Pageable pageable) {
        return ResponseEntity.ok(service.listar(canalPedido, status, usuarioId, pageable));
    }

    @Operation(summary = "Busca pedido pelo Id",
            responses = {
                @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
                @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
            })
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscaPedidoPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscaPedidoPorId(id));
    }

    @Operation(summary = "Atualiza status do pedido",
            description = "Transições válidas: PENDENTE → EM_PREPARO ou CANCELADO | EM_PREPARO → PRONTO ou CANCELADO | PRONTO → ENTREGUE",
            responses = {
                @ApiResponse(responseCode = "200", description = "Status atualizado"),
                @ApiResponse(responseCode = "409", description = "Transição de status inválida"),
                @ApiResponse(responseCode = "403", description = "Sem permissão"),
                @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
            })
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COZINHA', 'ATENDENTE')")
    @PutMapping("/{id}/status")
    public ResponseEntity<PedidoResponse> atualizarStatus(@PathVariable Integer id,@Valid @RequestBody StatusPedidoRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(service.atualizarStatus(id, request, userDetails.getUsername()));
    }

}
