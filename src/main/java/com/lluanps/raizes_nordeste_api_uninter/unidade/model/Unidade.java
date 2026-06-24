package com.lluanps.raizes_nordeste_api_uninter.unidade.model;

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
public class Unidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private String endereco;
    private String telefone;
    private boolean ativo;
    private LocalDateTime criadoEm;

    @PrePersist
    public void onCreate() {
        criadoEm = LocalDateTime.now();
        if(!ativo) {
            ativo = true;
        }
    }

}
