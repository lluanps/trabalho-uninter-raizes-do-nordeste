package com.lluanps.raizes_nordeste_api_uninter.fidelidade.model;

import com.lluanps.raizes_nordeste_api_uninter.usuario.model.Usuario;
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
public class Fidelidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    private Integer saldoPontos;
    private boolean consentimentoAtivo;
    private LocalDateTime consentimentoData;
    private LocalDateTime atualizadoEm;

    @PrePersist
    @PreUpdate
    protected void onSave() {
        atualizadoEm = LocalDateTime.now();
        if (saldoPontos == null) saldoPontos = 0;
    }

}
