# Modelo de Dados (ERD)

Diagrama entidade-relacionamento da base **PostgreSQL 15 (RDS)**, schema `public`.

```mermaid
erDiagram
    CLIENTES {
        bigint id PK
        varchar value "CPF ou CNPJ"
        varchar email
        varchar nome
        varchar telefone
    }
    VEICULOS {
        bigint id PK
        bigint cliente_id FK
        integer ano
        varchar marca
        varchar modelo
        varchar value "placa"
    }
    SERVICOS {
        bigint id PK
        varchar descricao
        varchar nome
        numeric preco
    }
    PECAS {
        bigint id PK
        varchar descricao
        varchar nome
        numeric preco
        integer qtd_estoque
    }
    ORDEM_SERVICOS {
        bigint id PK
        bigint cliente_id FK
        bigint veiculo_id FK
        timestamp criado_em
        timestamp finalizado_em
        varchar status "RECEBIDA|EM_DIAGNOSTICO|AGUARDANDO_APROVACAO|EM_EXECUCAO|FINALIZADA|ENTREGUE|RECUSADA"
        numeric total_orcamento
    }
    ORDEM_SERVICOS_SERVICOS {
        bigint ordem_servico_id PK,FK
        bigint servico_id PK,FK
    }
    ORDEM_SERVICOS_PECAS {
        bigint ordem_servico_id PK,FK
        bigint peca_id PK,FK
    }

    CLIENTES ||--o{ VEICULOS : possui
    CLIENTES ||--o{ ORDEM_SERVICOS : abre
    VEICULOS ||--o{ ORDEM_SERVICOS : associa
    ORDEM_SERVICOS ||--o{ ORDEM_SERVICOS_SERVICOS : "inclui serviços"
    SERVICOS ||--o{ ORDEM_SERVICOS_SERVICOS : "é incluído em"
    ORDEM_SERVICOS ||--o{ ORDEM_SERVICOS_PECAS : "inclui peças"
    PECAS ||--o{ ORDEM_SERVICOS_PECAS : "é incluída em"
```

## Observações

- `clientes.value` guarda o documento sem máscara; `cliente.status` **não existe** no
  schema (a Lambda retorna `status: "ATIVO"` de forma implícita).
- O orçamento (`total_orcamento`) é **calculado** no domínio: soma dos preços de serviços
  + peças (com margem de lucro aplicada na criação da peça).
- `ordem_servicos.status` controla o ciclo de vida da OS
  (`RECEBIDA → EM_DIAGNOSTICO → AGUARDANDO_APROVACAO → EM_EXECUCAO → FINALIZADA → ENTREGUE`,
  com recusa `AGUARDANDO_APROVACAO → RECUSADA`).
- DDL gerido pelo Hibernate (`ddl-auto: update`); FKs e índices criados automaticamente.
