CREATE TABLE IF NOT EXISTS log_auditoria (
    id BIGSERIAL PRIMARY KEY,
    usuario_id  BIGINT REFERENCES usuario(id),
    acao VARCHAR(150) NOT NULL,
    entidade VARCHAR(150),
    entidade_id BIGINT,
    detalhes TEXT,
    criado_em TIMESTAMP NOT NULL DEFAULT NOW()
);