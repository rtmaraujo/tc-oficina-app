# ADR-0005 - Estratégia de ambientes e branchs (main + homologacao)

- **Status**: Aceito
- **Data**: 2026-08-13

## Contexto

O tech challenge exige ambiente de **homologação** separado de **produção**, com deploy
automático via CI/CD a partir de branchs.

## Decisão

Duas branchs de deploy em todos os repositórios:

| Branch | Ambiente | Namespace k3s | Portas | Stack SAM |
|--------|----------|---------------|--------|-----------|
| `main` | produção | `tc-oficina` | app `30080`, auth `30082` | `tc-oficina-auth` |
| `homologacao` | homologação | `tc-oficina-homolog` | app `30081`, auth `30083` | `tc-oficina-auth-homolog` |

- Branch `main` é **protegida**: mudanças apenas via Pull Request.
- `homologacao` é fast-forward a partir de `main` após o merge do PR.

## Justificativa

- Isolamento entre ambientes evita que validações quebrem produção.
- Smoke tests (healthcheck + chamada real da Lambda) rodam em ambos os ambientes.
- Mesmos manifestos parametrizados por `NAMESPACE`/portas — evita divergência de infra.

## Consequências

- Manifestos `k8s/*.yaml` usam `envsubst` (`${NAMESPACE}`, `${NODE_PORT}`).
- GitHub **environments** `producao` e `homologacao` com secrets próprios.
- O CI cria o namespace antes de aplicar o `regcred` (ECR imagePullSecret).
