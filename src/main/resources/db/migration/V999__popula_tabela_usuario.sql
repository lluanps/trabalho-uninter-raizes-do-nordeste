INSERT INTO usuario (nome, email, senha, cpf, perfil, consentimento_lgpd, consentimento_data, ativo, criado_em, atualizado_em)
VALUES
  ('Administrador', 'admin@raizesdonordeste.com.br',
   '$2a$10$rAehWFLvHib4dP6ndzO68u93wT38j5XK2uemenO.LSejaEsKn3Fbu',   -- Admin@123
   '000.000.000-01', 'ADMIN', true, NOW(), true, NOW(), NOW()),
  ('Gerente Recife', 'gerente@raizesdonordeste.com.br',
   '$2a$10$V7vH2Wj4QreXPtI0XDLlXOA4oZek0KbUYSHcO60GR878XvcjIIyGy',   -- Gerente@123
   '000.000.000-02', 'GERENTE', true, NOW(), true, NOW(), NOW()),
  ('Cliente Teste', 'cliente@raizesdonordeste.com.br',
   '$2a$10$AaAnuexpWeGDPipGb0k6PuO6JkSAZhK8ZdW0iQPhTd7P8g04OayUa',   -- Cliente@123
   '000.000.000-03', 'CLIENTE', true, NOW(), true, NOW(), NOW()),
  ('Cozinheiro', 'cozinha@raizesdonordeste.com.br',
   '$2a$10$rAehWFLvHib4dP6ndzO68u93wT38j5XK2uemenO.LSejaEsKn3Fbu',   -- Admin@123
   '000.000.000-04', 'COZINHA', true, NOW(), true, NOW(), NOW()),
  ('Atendente', 'atendente@raizesdonordeste.com.br',
   '$2a$10$rAehWFLvHib4dP6ndzO68u93wT38j5XK2uemenO.LSejaEsKn3Fbu',   -- Admin@123
   '000.000.000-05', 'ATENDENTE', true, NOW(), true, NOW(), NOW());