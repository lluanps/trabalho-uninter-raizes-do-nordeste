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

CREATE TABLE IF NOT EXISTS fidelidade (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE REFERENCES usuario(id),
    saldo_pontos INTEGER NOT NULL DEFAULT 0,
    consentimento_ativo BOOLEAN NOT NULL DEFAULT FALSE,
    consentimento_data TIMESTAMP,
    atualizado_em TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE movimentacao_pontos_fidelidade (
    id BIGSERIAL PRIMARY KEY,
    fidelidade_id BIGINT NOT NULL REFERENCES fidelidade(id),
    pedido_id BIGINT REFERENCES pedido(id),
    tipo VARCHAR(10) NOT NULL CHECK (tipo IN ('CREDITO', 'DEBITO')),
    pontos INTEGER NOT NULL,
    descricao TEXT,
    criado_em TIMESTAMP NOT NULL DEFAULT NOW()
);
