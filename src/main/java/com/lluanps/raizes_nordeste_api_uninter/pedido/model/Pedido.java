package com.lluanps.raizes_nordeste_api_uninter.pedido.model;

import com.lluanps.raizes_nordeste_api_uninter.enums.CanalPedido;
import com.lluanps.raizes_nordeste_api_uninter.enums.StatusPedido;
import com.lluanps.raizes_nordeste_api_uninter.unidade.model.Unidade;
import com.lluanps.raizes_nordeste_api_uninter.usuario.model.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_id", nullable = false)
    private Unidade unidade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPedido status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private CanalPedido canal;

    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal desconto;

    @Column(name = "pontos_utilizados", nullable = false)
    private Integer pontosUtilizados;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
        atualizadoEm = LocalDateTime.now();
        if (status == null) status = StatusPedido.PENDENTE;
        if (valorTotal == null) valorTotal = BigDecimal.ZERO;
        if (desconto == null) desconto = BigDecimal.ZERO;
        if (pontosUtilizados == null) pontosUtilizados = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        atualizadoEm = LocalDateTime.now();
    }

}
