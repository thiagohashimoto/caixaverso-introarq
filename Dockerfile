# ============================================================
# Stage 1: Build - Compila a aplicação Java com Maven
# ============================================================
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copia o pom.xml e baixa dependências (cache layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia código-fonte
COPY src ./src

# Compila a aplicação
RUN mvn clean package -DskipTests -B

# ============================================================
# Stage 2: Runtime - Executa a aplicação compilada
# ============================================================
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Instala ferramentas úteis
RUN apk add --no-cache \
    curl \
    dumb-init \
    && rm -rf /var/cache/apk/*

# Copia o JAR compilado do stage anterior
COPY --from=builder /app/target/minierp-*.jar app.jar

# Expõe a porta
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
    CMD curl -f http://localhost:8080/health || exit 1

# Variáveis de ambiente padrão (podem ser sobrescritas)
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Executa a aplicação
ENTRYPOINT ["/usr/bin/dumb-init", "--"]
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
