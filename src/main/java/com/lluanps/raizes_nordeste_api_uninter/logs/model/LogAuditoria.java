package com.lluanps.raizes_nordeste_api_uninter.logs.model;

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
public class LogAuditoria {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "usuario_id")
        private Usuario usuario;

        private String acao;
        private String entidade;
        private Integer entidadeId;
        private String detalhes;
        private LocalDateTime criadoEm;

        @PrePersist
        protected void onCreate() {
        criadoEm = LocalDateTime.now();
    }

}
