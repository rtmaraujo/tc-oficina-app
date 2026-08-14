# RFC-0004 - Observabilidade (New Relic + Logs JSON)

- **Autor**: Equipe TC Oficina
- **Data**: 2026-08-13
- **Status**: Aceito

## Resumo

Definir a estrategia de monitoramento, logs estruturados e alertas para a Fase 3.

## Contexto

A Fase 3 exige monitorar latencia das APIs, consumo de CPU/memoria do Kubernetes,
healthchecks/uptime, alertas para falhas no processamento de ordens de servico e
logs estruturados (JSON) com correlacao entre requisicoes.

## Opcoes consideradas

| Opcao | Prós | Contras |
|-------|------|---------|
| **New Relic (APM + infra + logs)** | Agente Java simples (`javaagent`), dashboards prontos, integracao k3s | Licenca; limite de dados no plano gratuito |
| Datadog | Excelente observabilidade | Custo elevado para uso academico |
| CloudWatch puro | Nativo AWS, sem custo extra de licenca | Sem APM de latencia por endpoint e dashboards "prontos" tao ricos |

## Decisao

Utilizar **New Relic**:
- Agente Java via `javaagent` no Dockerfile da app e do container de auth;
- Envs `NEW_RELIC_APP_NAME` / `NEW_RELIC_LICENSE_KEY` injetadas via ConfigMap/Secret no k3s;
- **Logs estruturados JSON** via `logstash-logback-encoder` (app Spring e Lambda),
  com `correlation_id` em MDC para correlacionar requisicoes;
- HPA (CPU 70% / mem 80%) e probes de liveness/readiness para saude dos pods;
- Dashboards New Relic para volume de OS, tempo medio por status e erros de integracao.

## Consequencias

- Necessario manter a license key como secret nos repos (sem versionar).
- Logs JSON viram o padrao em todos os ambientes gerenciados (perfil `json` no Spring).
- A funcao Lambda em modo serverless ainda nao possui layer New Relic (pendencia aberta);
  o container de auth ja instrumenta.