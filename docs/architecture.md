# Arquitetura de Referência

Visão de componentes e fluxos da solução **tc-oficina** (Fase 3).

## Visão de Contexto

```mermaid
flowchart TB
    ADMIN["Admin (Gerente)"]
    CLIENTE["Cliente (público)"]

    subgraph PROD["Produção"]
        APPGW["API Gateway Traefik<br/>80/443 (k3s)"]
        LAMBDA["Auth Lambda<br/>Java 21 (opcional)"]
        K3S_APP["App Spring Boot<br/>k3s ns tc-oficina"]
        K3S_AUTH["Auth container<br/>k3s ns tc-oficina"]
        RDS[(RDS PostgreSQL 15<br/>privado)]
    end

    ADMIN -->|HTTPS| APPGW
    CLIENTE -->|HTTPS| APPGW
    APPGW -->|"GET /api/v1, /actuator, /swagger"| K3S_APP
    APPGW -->|"POST /auth {cpf}"| K3S_AUTH
    CLIENTE -.->|"POST /auth (serverless)"| LAMBDA
    LAMBDA -->|JDBC| RDS
    K3S_AUTH -->|JDBC| RDS
    K3S_APP -->|JDBC| RDS
    K3S_APP -->|consulta status| K3S_AUTH
```

## Visão de Deploy (AWS)

```mermaid
flowchart TB
    subgraph AWS["AWS us-west-2"]
        subgraph VPC["VPC 10.0.0.0/16"]
            IGW["Internet Gateway"]
            subgraph PUB["Subnets públicas"]
                EIP["EIP 35.84.122.229"]
                SRV["k3s server (t3.small)"]
                W1["worker-1 (t3.small)"]
                W2["worker-2 (t3.small)"]
            end
            subgraph PRIV["Subnets privadas"]
                RDS[(RDS PostgreSQL 15<br/>Multi-AZ backup)]
                ENI["Lambda ENI"]
            end
        end
        ECR["ECR tc-oficina"]
        CF["CloudFormation<br/>tc-oficina-auth / -homolog"]
        LAMBDA["Lambda auth (Java 21)<br/>VPC, SG egress 5432"]
        APIGW["API Gateway<br/>Prod /auth"]
    end

    SRV --> W1
    SRV --> W2
    RDS --> PRIV
    ENI --> RDS
    CF --> LAMBDA
    CF --> APIGW
    LAMBDA --> ENI
    SRV -.pull imagem.-> ECR
    W1 -.pull imagem.-> ECR
    W2 -.pull imagem.-> ECR
```

## Visão de Componentes (App Spring Boot)

```mermaid
flowchart LR
    subgraph presentation
        C[Controllers REST]
        DTO[DTOs request/response]
    end
    subgraph application
        S[Casos de uso @Service]
    end
    subgraph domain
        M[Models de negócio]
        R[Interfaces de repositório]
        DS[Domain services]
    end
    subgraph infrastructure
        RI[RepoImpl Spring Data]
        E[JPA entities]
        MAP[Mapper JPA <-> Domain]
        JWT[Security/JWT]
    end

    C --> S
    S --> R
    R -.implementa.-> RI
    RI --> MAP
    MAP --> E
    E -->|JDBC| RDS[(PostgreSQL)]
    S --> DS
    DS --> M
    C --> DTO
```

## Fluxo de Autenticação (Lambda)

```mermaid
sequenceDiagram
    participant C as Cliente
    participant G as API Gateway
    participant L as Auth Lambda
    participant D as RDS PostgreSQL

    C->>G: POST /auth {"cpf":"12345678909"}
    G->>L: invoke (proxy)
    L->>L: valida CPF (dígitos verificadores)
    L->>D: SELECT nome FROM clientes WHERE value = ?
    D-->>L: nome do cliente
    L->>L: gera JWT HS256 (JWT_SECRET)
    L-->>G: 200 {access_token, nome, status:ATIVO}
    G-->>C: 200 JSON
```

## Fluxo de Deploy (CI/CD)

```mermaid
flowchart LR
    PR[PR para main] -->|build + testes| CI
    MERGE[Merge na main] -->|push| PIPE
    PIPE[GitHub Actions] --> BUILD[Build + push ECR]
    PIPE --> DEPLAMBDA[SAM deploy Lambda/API GW]
    PIPE --> DEPK8S[Apply manifestos k3s]
    DEPLAMBDA --> SMOKE[Smoke POST /auth]
    DEPK8S --> HEALTH[Healthcheck /actuator/health]
```

## Endpoints em Produção

Todas as requisições passam pelo **API Gateway Traefik** (ponto único de entrada no k3s):

| Serviço | Endpoint |
|---------|----------|
| App API + Swagger | `http://35.84.122.229/swagger-ui/index.html` |
| App health | `http://35.84.122.229/actuator/health` |
| Auth (via gateway) | `http://35.84.122.229/auth` |
| Homologação (via gateway) | `http://35.84.122.229:8081/...` |
| Auth Lambda (API Gateway AWS, opcional) | `https://8rfjx5ofoi.execute-api.us-west-2.amazonaws.com/Prod/auth` |
| Auth Lambda (homolog) | `https://6116yqil7i.execute-api.us-west-2.amazonaws.com/Prod/auth` |

## Ambientes

| Ambiente | Namespace | API Gateway | Lambda stack |
|----------|-----------|-------------|--------------|
| Produção | `tc-oficina` | `:80` / `:443` | `tc-oficina-auth` |
| Homologação | `tc-oficina-homolog` | `:8081` | `tc-oficina-auth-homolog` |
