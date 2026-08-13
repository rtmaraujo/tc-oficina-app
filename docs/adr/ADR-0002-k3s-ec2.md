# ADR-0002 - Cluster Kubernetes k3s em EC2 (autogerenciado)

- **Status**: Aceito
- **Data**: 2026-08-13

## Contexto

A fase 3 do tech challenge exige um cluster Kubernetes para execução da aplicação em
produção. O ambiente de laboratório (AWS Academy Learner Lab) possui restrições de
serviço (ex.: não permite criar roles IAM livremente nem clusters gerenciados).

## Decisão

Provisionar um cluster **k3s autogerenciado em EC2** (1 servidor + 2 workers, instâncias
`t3.small`, Ubuntu 24.04) via Terraform no repositório `tc-oficina-infra-k8s`.

## Justificativa

- **k3s é Kubernetes real** (Kubernetes certificado), com `metrics-server` embutido para
  HPA — atende integralmente ao requisito de orquestração e autoscaling.
- **Baixo custo**: 3 instâncias `t3.small` (~1.9 Gi allocatable cada) suficientes para
  app (2–4 réplicas de 512 Mi) + auth (2 réplicas de 384 Mi) + homologação.
- **Compatível com o Learner Lab**: usa apenas EC2, VPC, EIP, ECR — serviços permitidos.
- Terraform com `userdata` (script de bootstrap) garante cluster reproduzível.

## Consequências

- **HPA limitado a `maxReplicas: 4`** (ADR-0003) para caber na capacidade do cluster.
- O acesso SSH (`porta 22` + chave) é usado pelo CI/CD para aplicar manifestos.
- NodePort expõe app e auth: produção `30080`/`30082`, homologação `30081`/`30083`.
