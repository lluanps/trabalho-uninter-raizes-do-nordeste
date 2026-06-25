CREATE TABLE IF NOT EXISTS pedido (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES usuario(id),
    unidade_id BIGINT NOT NULL REFERENCES unidade(id),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE'
        CHECK (status IN ('PENDENTE','EM_PREPARO','PRONTO','ENTREGUE','CANCELADO')),
    canal VARCHAR(10) NOT NULL
        CHECK (canal IN ('APP','TOTEM','BALCAO','PICKUP','WEB')),
    valor_total DECIMAL(10,2) NOT NULL DEFAULT 0,
    desconto DECIMAL(10,2) NOT NULL DEFAULT 0,
    pontos_utilizados INTEGER NOT NULL DEFAULT 0,
    criado_em TIMESTAMP NOT NULL DEFAULT NOW(),
    atualizado_em TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS item_pedido (
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT NOT NULL REFERENCES pedido(id),
    produto_id BIGINT NOT NULL REFERENCES produto(id),
    quantidade INTEGER NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS historico_status_pedido (
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT NOT NULL REFERENCES pedido(id),
    usuario_id BIGINT NOT NULL REFERENCES usuario(id),
    status_anterior VARCHAR(20),
    status_novo VARCHAR(20) NOT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT NOW()
)