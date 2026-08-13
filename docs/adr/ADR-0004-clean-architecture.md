# ADR-0004 - Arquitetura em camadas (Clean Architecture) e fluxo de status da OS

- **Status**: Aceito
- **Data**: 2026-08-13

## Contexto

A aplicação cresce com múltiplas entidades de domínio e regras de negócio
(orçamento automático, fluxo de status da ordem de serviço, validação de CPF/placa).

## Decisão

Organizar o código em **Clean Architecture** com 4 camadas:

- `domain` — modelos POJO, regras de negócio puras (sem dependências de framework)
- `application` — casos de uso / serviços transacionais
- `infrastructure` — JPA, repositórios, security/JWT, config
- `presentation` — controllers REST, DTOs, mappers

## Justificativa

- **Testabilidade**: domínio puro permite 400+ testes e 90% de cobertura JaCoCo.
- **Independente de framework**: banco e transporte podem evoluir sem alterar o domínio.
- **Consistência de negócio**: fluxo de status da OS encapsulado no modelo
  (`RECEBIDA → EM_DIAGNOSTICO → AGUARDANDO_APROVACAO → EM_EXECUCAO → FINALIZADA → ENTREGUE`).

## Consequências

- Mappers explícitos entre domínio ↔ JPA e domínio ↔ DTO.
- `@Transactional` concentrado na camada de application.
- Regras de validação (CPF/CNPJ, placa Mercosul) vivem no domínio.
