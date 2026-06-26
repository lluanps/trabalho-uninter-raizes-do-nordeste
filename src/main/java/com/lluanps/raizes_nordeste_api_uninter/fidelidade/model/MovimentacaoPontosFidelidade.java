package com.lluanps.raizes_nordeste_api_uninter.fidelidade.model;

import com.lluanps.raizes_nordeste_api_uninter.enums.TipoMovimentacaoPontos;
import com.lluanps.raizes_nordeste_api_uninter.pedido.model.Pedido;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimentacaoPontosFidelidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fidelidade_id", nullable = false)
    private Fidelidade programaFidelidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TipoMovimentacaoPontos tipo;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    private Integer pontos;
    private String descricao;

    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
    }

}
