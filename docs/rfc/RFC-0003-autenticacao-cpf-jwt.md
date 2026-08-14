# RFC-0003 - Estrategia de Autenticacao (CPF + JWT via Function Serverless)

- **Autor**: Equipe TC Oficina
- **Data**: 2026-08-13
- **Status**: Aceito

## Resumo

Definir como proteger as rotas sensiveis da aplicacao usando autenticacao via CPF.

## Contexto

O requisito da Fase 3 pede:
- Proteger rotas sensiveis com autenticacao via **CPF**;
- Uma **function serverless** que valide o CPF, consulte o status do cliente e devolva um
  **token JWT** para consumo das APIs protegidas;
- Exposicao por **API Gateway**.

A aplicacao Fase 2 usava login admin/senha gerando JWT proprio (`/api/v1/auth/login`).

## Opcoes consideradas

| Opcao | Prós | Contras |
|-------|------|---------|
| **Lambda + API Gateway + JWT HS256** | Cumpre o requisito de serverless + API Gateway; JWT valido para a app (mesma secret) | Lambda dentro da VPC exige SG e subnets privadas |
| Manter so login admin/senha | Nada novo a implementar | Nao atende ao requisito de "auth via CPF + serverless" |
| Cognito User Pools | Servico gerenciado | Nao demonstra function serverless propria exigida |

## Decisao

Implementar `POST /auth {"cpf": ...}` em uma **AWS Lambda (Java 21)** exposta por
**API Gateway** (`AWS::Serverless::Api`):

1. `CpfValidator` valida os digitos verificadores do CPF;
2. `ClienteService` consulta `clientes` no RDS (via JDBC) e retorna o status;
3. `JwtService` gera JWT HS256 com `subject` = CPF e claim `status`, mesma
   `JWT_SECRET` usada pela aplicacao principal;
4. Resposta: `{cpf, nome, status, access_token, token_type, expires_in}`.

A aplicacao mantem o filtro `JwtAuthenticationFilter` que valida o mesmo JWT para as
rotas `/api/v1/admin/**`.

## Consequencias

- Duas vias de auth coexistem (admin/senha legacy + CPF via Lambda) — documentado como
  caminho de migracao.
- JWT expira em 86400s; a mesma secret deve ser compartilhada entre Lambda e app.
- CI executa smoke test real contra o endpoint do API Gateway com o CPF de teste `12345678909`.