# ADR-0003 - Autenticação via JWT com função serverless (Lambda + API Gateway)

- **Status**: Aceito
- **Data**: 2026-08-13

## Contexto

A autenticação precisa validar o CPF do cliente, consultar a base e emitir um token para
consumo das APIs protegidas. A arquitetura deveria demonstrar uma **function serverless
real** com **API Gateway**.

## Decisão

Autenticar via **CPF** através de uma **AWS Lambda (Java 21)** exposta por **API Gateway**:

- `POST /auth` recebe `{"cpf": "12345678909"}`
- Valida o CPF, consulta `clientes` no RDS e gera um **JWT HS256** com a mesma secret da aplicação
- Implantada via **AWS SAM/CloudFormation** (stack `tc-oficina-auth`)

## Justificativa

- Cumpre o requisito de "function serverless + API Gateway" do tech challenge.
- Escala automaticamente e cobra por invocação (fit no Learner Lab).
- O mesmo código também roda como **container** (`Main.java`) no k3s para health checks
  e consumo interno — reuso do `ClienteService`/`JwtService`.

## Consequências

- A Lambda roda **dentro da VPC** (subnets privadas) com SG de egress apenas para o RDS.
- Nomes de recursos parametrizados por `AWS::StackName` para permitir múltiplos ambientes
  (`tc-oficina-auth` = prod, `tc-oficina-auth-homolog` = homolog).
- Smoke test no CI chama o endpoint real do API Gateway (com retry para propagação).
