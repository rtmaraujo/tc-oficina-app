# RFC-0001 - Escolha da Nuvem (AWS)

- **Autor**: Equipe TC Oficina
- **Data**: 2026-08-13
- **Status**: Aceito

## Resumo

Definir a plataforma de nuvem onde a solucao da Fase 3 sera hospedada.

## Contexto

O Tech Challenge da Fase 3 exige API Gateway, function serverless, banco gerenciado,
cluster Kubernetes e Terraform. A infraestrutura real utilizada no curso (Learner Lab)
determina quais servicos estao disponiveis de forma gratuita e pratica.

## Opcoes consideradas

| Opcao | Prós | Contras |
|-------|------|---------|
| **AWS (Learner Lab)** | Servicos de `serverless` e banco gerenciado nativos; conta do curso disponivel | Credenciais temporarias (~1h); custo se nao desligar EC2 |
| GCP | Kubernetes (GKE) maduro | Sem Learner Lab; custo maior fora da conta do curso |
| Azure | Integracao com ecossistema Microsoft | Sem Learner Lab; curva de adocao do grupo |

## Decisao

Utilizar **AWS** na regiao `us-west-2`, aproveitando a conta do Learner Lab
(provides: Lambda, API Gateway, RDS PostgreSQL, EC2 para k3s, ECR, S3).

## Consequencias

- Credenciais temporarias precisam ser renovadas a cada sessao (`setup-aws-creds.ps1`).
- Instancias EC2 e RDS devem ser paradas fora do horario para economia de custo.
- Backend de estado do Terraform em S3 (`tc-oficina-terraform-state`).
