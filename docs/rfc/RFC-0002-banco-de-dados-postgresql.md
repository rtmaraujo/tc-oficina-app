# RFC-0002 - Escolha do Banco de Dados (PostgreSQL gerenciado)

- **Autor**: Equipe TC Oficina
- **Data**: 2026-08-13
- **Status**: Aceito

## Resumo

Definir o banco de dados gerenciado para a aplicacao principal e para a Lambda de autenticacao.

## Contexto

A aplicacao (Spring Boot + JPA/Hibernate) precisa de um banco relacional com consistencia
entre ordens de servico, clientes, veiculos, servicos e pecas. A Fase 3 exige um
**banco gerenciado** (nao container) provisionado por Terraform.

## Opcoes consideradas

| Opcao | Prós | Contras |
|-------|------|---------|
| **PostgreSQL (RDS)** | SQL robusto, JSONB, compatibilidade total com Hibernate; engine ja usada nas fases 1-2 | Gerenciamento de parametros |
| MySQL (RDS) | Amplamente conhecido | Menor aderencia ao schema relacional rico ja modelado |
| DynamoDB | Serverless e escalavel | Nao relacional; exigiria reescrever a modelagem da Fase 2 |

## Decisao

Utilizar **Amazon RDS PostgreSQL 15** (`db.t3.micro`, gp3 criptografado, backup 7 dias)
em subnets privadas, com SG liberando apenas a porta 5432 para a VPC `10.0.0.0/16`.

## Consequencias

- Lambda de autenticacao roda na mesma VPC para alcancar o RDS.
- Multi-AZ fica disponivel via variavel (`multi_az`) para elevacao de disponibilidade.
- Credenciais gerenciadas como secret do GitHub e injetadas via Terraform/K8s, nunca em arquivo.