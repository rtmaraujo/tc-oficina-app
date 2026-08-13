# ADR-0001 - Banco de dados PostgreSQL gerenciado

- **Status**: Aceito
- **Data**: 2026-08-13

## Contexto

A aplicação de gestão de oficina mecânica precisa de um banco relacional para armazenar
clientes, veículos, serviços, peças e ordens de serviço com forte integridade referencial
e consistência transacional.

## Decisão

Utilizar **PostgreSQL 15** gerenciado na AWS (**Amazon RDS**), provisionado via Terraform
no repositório `tc-oficina-infra-db`.

## Justificativa

- **ACID completo** com transações confiáveis — essencial para o fluxo de ordens de
  serviço (orçamento, aprovação, execução).
- **Integridade referencial** via FKs (`ordem_servicos → clientes/veiculos`, tabelas de
  junção `ordem_servicos_pecas` e `ordem_servicos_servicos`).
- **Gerenciado pela AWS**: backups automáticos, Multi-AZ e storage criptografado (gp3)
  sem custo operacional de administração.
- **Open source** e amplamente suportado por Spring Data JPA.

## Consequências

- Tabelas criadas via `ddl-auto: update` do Hibernate: `clientes`, `veiculos`, `servicos`,
  `pecas`, `ordem_servicos`, `ordem_servicos_pecas`, `ordem_servicos_servicos`.
- RDS em subnets privadas, acessível apenas dentro da VPC (SG libera 5432 para
  `10.0.0.0/16`), reforçando o princípio do menor privilégio.
