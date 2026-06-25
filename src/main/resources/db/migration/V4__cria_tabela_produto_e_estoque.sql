CREATE TABLE IF NOT EXISTS produto (
    id BIGSERIAL PRIMARY KEY,
    unidade_id BIGINT NOT NULL REFERENCES unidade(id),
    nome VARCHAR(150) NOT NULL,
    descricao TEXT,
    preco DECIMAL(10,2) NOT NULL,
    categoria VARCHAR(100),
    imagem_url VARCHAR(500),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP NOT NULL DEFAULT NOW(),
    atualizado_em TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS estoque (
    id BIGSERIAL PRIMARY KEY,
    produto_id BIGINT NOT NULL REFERENCES produto(id),
    unidade_id BIGINT NOT NULL REFERENCES unidade(id),
    version BIGINT NOT NULL DEFAULT 0,
    quantidade INTEGER NOT NULL DEFAULT 0,
    atualizado_em TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (produto_id, unidade_id)
);

CREATE TABLE IF NOT EXISTS movimentacao_estoque (
    id BIGSERIAL PRIMARY KEY,
    estoque_id BIGINT NOT NULL REFERENCES estoque(id),
    usuario_id BIGINT NOT NULL REFERENCES usuario(id),
    tipo VARCHAR(10) NOT NULL CHECK (tipo in ('ENTRADA', 'SAIDA')),
    quantidade INTEGER NOT NULL,
    observacao TEXT,
    criado_em TIMESTAMP NOT NULL DEFAULT NOW()
);
