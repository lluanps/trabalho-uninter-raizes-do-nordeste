# Raízes do Nordeste — API

**Aluno:** Luan Pinheiro da Silva  
**RU:** 4323854  
**Polo:** São José - SC  
**Doc Swagger:** http://localhost:8080/swagger-ui/swagger-ui/index.html

---

Projeto da disciplina de Projeto Multidisciplinar (Uninter), trilha Back-end.

A ideia é simular o back-end de uma rede de lanchonetes nordestinas, com controle de estoque por unidade, pedidos via múltiplos canais, pagamento mock e programa de fidelidade com consentimento LGPD.

---

## O que precisa ter instalado

- Java 17
- Maven 3.8+
- PostgreSQL 14+

---

## Como rodar

**1. Crie o banco de dados**

```sql
CREATE DATABASE raizes_nordeste;
```

**2. Configure o `application.properties`**

O arquivo fica em `src/main/resources/application.properties`. Ajuste com os dados do seu PostgreSQL:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/raizes_nordeste
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
jwt.secret=coloque-uma-chave-longa-aqui-com-pelo-menos-32-chars
jwt.expiration=86400000
```

**3. Suba a aplicação**

```bash
mvn spring-boot:run
```

As tabelas são criadas automaticamente pelo Flyway. Na primeira execução, um seed com 5 usuários já é inserido.

---

## Swagger

Com a aplicação rodando:

```
http://localhost:8080/swagger-ui/index.html
```

Para testar endpoints protegidos, faça o login, copie o token e clique em **Authorize** no topo da página.

---

## Usuários disponíveis para teste

| Email | Senha | Perfil |
|---|---|---|
| admin@raizesdonordeste.com.br | Admin@123 | ADMIN |
| gerente@raizesdonordeste.com.br | Admin@123 | GERENTE |
| cliente@raizesdonordeste.com.br | Admin@123 | CLIENTE |
| cozinha@raizesdonordeste.com.br | Admin@123 | COZINHA |
| atendente@raizesdonordeste.com.br | Admin@123 | ATENDENTE |

---

## Coleção Postman

O arquivo `raizes-nordeste.postman_collection.json` está na raiz do repositório.

Importe no Postman e faça o login primeiro — o token é salvo automaticamente e usado nos demais requests.

Fluxo sugerido para testar o sistema do começo ao fim:

1. Login (`POST /auth/login`)
2. Criar unidade (`POST /unidades`)
3. Criar produto (`POST /unidades/{id}/produtos`)
4. Dar entrada no estoque (`POST /unidades/{id}/estoque`)
5. Criar pedido com canal (`POST /pedidos`)
6. Processar pagamento mock (`POST /pedidos/{id}/pagamento`)
7. Atualizar status do pedido (`PUT /pedidos/{id}/status`)
8. Consultar pontos de fidelidade (`GET /fidelidade/saldo`)

---

## Tecnologias

- Spring Boot 3.5 / Java 17
- Spring Security + JWT
- Spring Data JPA + PostgreSQL
- Flyway (migrations)
- SpringDoc OpenAPI (Swagger UI)
- Lombok
