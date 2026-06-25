package com.lluanps.raizes_nordeste_api_uninter.pedido.controller;

import com.lluanps.raizes_nordeste_api_uninter.enums.CanalPedido;
import com.lluanps.raizes_nordeste_api_uninter.enums.StatusPedido;
import com.lluanps.raizes_nordeste_api_uninter.pedido.dto.CriaPedidoRequest;
import com.lluanps.raizes_nordeste_api_uninter.pedido.dto.PedidoResponse;
import com.lluanps.raizes_nordeste_api_uninter.pedido.dto.StatusPedidoRequest;
import com.lluanps.raizes_nordeste_api_uninter.pedido.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService service;

    @PostMapping
    public ResponseEntity<PedidoResponse> registraNovoPedido(@Valid @RequestBody CriaPedidoRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(service.registraNovoPedido(request, userDetails.getUsername()));
    }

    @GetMapping
    public ResponseEntity<Page<PedidoResponse>> listar(@RequestParam(required = false) CanalPedido canalPedido, @RequestParam(required = false) StatusPedido status,
            @RequestParam(required = false) Integer usuarioId, Pageable pageable) {
        return ResponseEntity.ok(service.listar(canalPedido, status, usuarioId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscaPedidoPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscaPedidoPorId(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PedidoResponse> atualizarStatus(@PathVariable Integer id,@Valid @RequestBody StatusPedidoRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(service.atualizarStatus(id, request, userDetails.getUsername()));
    }

}
