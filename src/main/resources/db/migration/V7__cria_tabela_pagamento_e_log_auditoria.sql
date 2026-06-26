CREATE TABLE IF NOT EXISTS pagamento (
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT NOT NULL REFERENCES pedido(id),
    valor DECIMAL(10,2) NOT NULL,
    metodo_pagamento VARCHAR(50),
    status VARCHAR(15) NOT NULL DEFAULT 'PENDENTE'
        CHECK (status IN ('PENDENTE', 'APROVADO', 'RECUSADO', 'ERRO')),
    payload_requisicao TEXT,
    payload_resposta TEXT,
    criado_em TIMESTAMP NOT NULL DEFAULT NOW(),
    atualizado_em TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE log_auditoria (
    id BIGSERIAL PRIMARY KEY,
    usuario_id  BIGINT REFERENCES usuario(id),
    acao VARCHAR(150) NOT NULL,
    entidade VARCHAR(150),
    entidade_id BIGINT,
    detalhes TEXT,
    criado_em TIMESTAMP NOT NULL DEFAULT NOW()
);