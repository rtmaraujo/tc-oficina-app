# ============================================
# Stage 1: Builder - Compilar aplicação
# ============================================
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# Instalar Maven e dependências de build
RUN apt-get update && apt-get install -y --no-install-recommends maven curl unzip && rm -rf /var/lib/apt/lists/*

# Copiar apenas pom.xml primeiro para cache layer
COPY pom.xml .

# Baixar dependências (camada cacheable)
RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline -B -q

# Copiar código fonte
COPY src ./src

# Compilar e empacotar (sem testes) e renomear JAR para nome fixo
RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests -q && \
    cp target/*.jar target/app.jar

# Baixar e extrair o agente do New Relic (Java APM)
RUN curl -fsSL -o /tmp/newrelic-java.zip https://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic-java.zip && \
    unzip -q /tmp/newrelic-java.zip -d /opt && \
    rm -f /tmp/newrelic-java.zip

# ============================================
# Stage 2: Runtime - Imagem final otimizada
# ============================================
FROM eclipse-temurin:21-jre-alpine

ARG BUILD_DATE
ARG COMMIT_SHA

LABEL org.opencontainers.image.title="TC Oficina App" \
      org.opencontainers.image.description="Tech Challenge - Oficina Mecanica Backend" \
      org.opencontainers.image.created="${BUILD_DATE}" \
      org.opencontainers.image.revision="${COMMIT_SHA}"

WORKDIR /app

# Copiar JAR renomeado (nome fixo, independente da versão)
COPY --from=builder /app/target/app.jar .
COPY --from=builder /opt/newrelic /opt/newrelic

# Criar usuário non-root para segurança
RUN addgroup -g 1001 -S appuser && adduser -u 1001 -S appuser -G appuser && \
    chown -R appuser:appuser /opt/newrelic
USER appuser

EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Comando para executar (agente New Relic ativo via javaagent)
CMD ["java", "-javaagent:/opt/newrelic/newrelic.jar", "-jar", "app.jar"]
