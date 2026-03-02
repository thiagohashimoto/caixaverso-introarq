# CaixaVerso MiniERP

Sistema integrado de gestão empresarial com módulos de Vendas, Estoque e Financeiro. Construído com Spring Boot 3.2, arquitetura monolítica modular e infraestrutura containerizada com Nginx como reverse proxy.

---

## Stack

| Camada | Tecnologia |
|--------|-----------|
| Linguagem | Java 21 |
| Framework | Spring Boot 3.2.0 |
| Persistência | PostgreSQL 15 + Spring Data JPA + Hibernate |
| Segurança | Spring Security 6.x |
| Monitoramento | Spring Boot Actuator |
| Proxy / Segurança de rede | Nginx 1.25 |
| Mensageria (futura) | RabbitMQ 3.12 |
| Mapeamento DTOs | MapStruct |
| Build | Maven 3.9 |
| Containers | Docker + Docker Compose |

---

## Arquitetura

O projeto adota **monolito modular** — um único processo com módulos internos bem delimitados que se comunicam via eventos de domínio (Spring Events), sem acoplamento direto entre si.

```
┌─────────────────────────────────────────────────────────────┐
│                    Nginx Reverse Proxy                       │
│    Porta 80 (HTTP) · Porta 443 (HTTPS autoassinado)         │
│    Rate Limit · Gzip · Cache · Basic Auth · Headers         │
└───────────────────────┬─────────────────────────────────────┘
                        │ proxy_pass
┌───────────────────────▼─────────────────────────────────────┐
│               Spring Boot MiniERP (:8080/api)                │
│  ┌──────────┐   ┌──────────┐   ┌────────────┐               │
│  │  Vendas  │   │ Estoque  │   │ Financeiro │               │
│  └────┬─────┘   └────┬─────┘   └─────┬──────┘               │
│       └──────────────┴───────────────┘                       │
│              Spring Events (SAGA interno)                    │
└───────────────────────┬─────────────────────────────────────┘
                        │
               ┌────────▼────────┐
               │   PostgreSQL    │
               │   porta 5432    │
               └─────────────────┘
```

### Estrutura de pastas por módulo

Cada módulo segue a mesma estrutura em 4 camadas:

```
<modulo>/
├── api/
│   └── controller/       # REST Controllers (@RestController)
├── application/
│   ├── dto/              # Request/Response DTOs
│   ├── mapper/           # MapStruct (DTO ↔ Domain)
│   └── service/          # Application Services (casos de uso)
├── domain/
│   ├── model/            # Entidades e Value Objects
│   ├── event/            # Eventos de domínio
│   ├── service/          # Serviços de domínio
│   └── repository/       # Interfaces de repositório
└── infrastructure/
    ├── persistence/      # Implementação JPA dos repositórios
    ├── messaging/        # Event Listeners
    └── config/           # Configurações do módulo
```

### Fluxo SAGA — Criação de Venda

```
POST /api/vendas
  └─ VendaController
       └─ VendaApplicationService
            └─ Cria Venda (PENDENTE)
                 └─ Publica VendaCriadaEvent
                      ├─ EstoqueEventListener
                      │    ├─ Reserva estoque
                      │    ├─ [OK]  → publica EstoqueReservadoEvent
                      │    └─ [FAIL]→ publica VendaCanceladaEvent (compensação)
                      │
                      └─ FinanceiroEventListener escuta EstoqueReservadoEvent
                           ├─ Cria Cobrança
                           ├─ [OK]  → publica CobrancaCriadaEvent
                           └─ [FAIL]→ libera estoque + cancela venda

VendaEventListener escuta CobrancaCriadaEvent
  └─ Venda: PENDENTE → CONFIRMADA
```

---

## Pré-requisitos

- **Docker Desktop** 4.x ou superior
- **Docker Compose** v2

> Para desenvolvimento local sem Docker: Java 21 + Maven 3.9

---

## Inicializar a aplicação

### Com Docker (recomendado)

```bash
# 1. Clonar o repositório
git clone <url-do-repositorio>
cd caixaverso-introarq

# 2. Subir todos os serviços
docker compose up -d

# 3. Acompanhar logs
docker compose logs -f minierp
```

Na primeira execução o Maven fará download das dependências (~3 min). Após isso:

| Serviço | URL |
|---------|-----|
| API via Nginx (HTTP) | http://localhost/api |
| API via Nginx (HTTPS) | https://localhost/api |
| Health Check Nginx | http://localhost/health |
| Actuator (requer auth) | http://localhost/api/actuator/health |
| pgAdmin | http://localhost:5050 |
| RabbitMQ Management | http://localhost:15672 |

> A porta `8080` do Spring Boot **não está exposta** para o host. Todo acesso deve ser via Nginx (porta 80/443).

### Parar os serviços

```bash
docker compose down
```

### Rebuild após alterações no código

```bash
docker compose build --no-cache minierp
docker compose up -d
```

---

## Credenciais padrão

| Serviço | Usuário | Senha |
|---------|---------|-------|
| PostgreSQL | admin | admin123 |
| pgAdmin | admin@admin.com | admin |
| RabbitMQ | guest | guest |
| Nginx Basic Auth (Actuator/Swagger) | admin | admin123 |

---

## API REST

A API está disponível em `http://localhost/api/` (via Nginx proxy).

### Estoque

```http
# Criar produto
POST /api/estoque/produtos
Content-Type: application/json

{
  "nome": "Notebook Dell",
  "quantidade": 50,
  "valor": 3500.00
}

# Listar produtos
GET /api/estoque/produtos

# Buscar por ID
GET /api/estoque/produtos/{id}
```

### Vendas

```http
# Criar venda
POST /api/vendas
Content-Type: application/json

{
  "clienteId": 1,
  "itens": [
    {
      "produtoId": 1,
      "quantidade": 2,
      "valor": 3500.00
    }
  ]
}

# Listar vendas
GET /api/vendas

# Buscar venda por ID
GET /api/vendas/{id}

# Confirmar venda
PUT /api/vendas/{id}/confirmar
```

### Financeiro

```http
# Listar cobranças
GET /api/financeiro/cobrancas

# Buscar cobrança por ID
GET /api/financeiro/cobrancas/{id}

# Registrar pagamento
PUT /api/financeiro/cobrancas/{id}/pagar
```

### Health / Actuator

```http
# Health check do Nginx (sem auth)
GET http://localhost/health

# Health check da aplicação (requer Basic Auth: admin / admin123)
GET http://localhost/api/actuator/health
```

---

## Nginx — Funcionalidades implementadas

### Segurança
| Mecanismo | Configuração |
|-----------|-------------|
| Security Headers | `X-Content-Type-Options: nosniff`, `X-Frame-Options: DENY`, `X-XSS-Protection: 1; mode=block` |
| Rate Limiting | 5 req/s por IP, burst=10 — retorna **429** ao exceder |
| Limite de payload | `client_max_body_size 1m` — retorna **413** para requests > 1MB |
| Basic Auth | `/api/actuator` e `/api/swagger-ui` protegidos com htpasswd |
| HTTPS | TLS 1.2/1.3 com certificado autoassinado (porta 443) |
| Ocultar versão | `server_tokens off` |

### Performance
| Mecanismo | Configuração |
|-----------|-------------|
| Compressão GZIP | `gzip on` para `application/json` e `text/plain` |
| Cache GET | Respostas 200 cacheadas por 10s — header `X-Cache-Status: HIT/MISS` |
| Cache bypass | Requisições com `Authorization` nunca são cacheadas |
| Keepalive upstream | Pool de 16 conexões persistentes com o backend |
| Buffering | `proxy_buffering on` para suportar GZIP em respostas proxied |

### Observabilidade
```
# Log estruturado (acessível via docker logs)
docker logs caixaverso-nginx --tail 20

# Formato:
# IP [data] "METODO /uri HTTP/1.1" STATUS BYTES rt=TEMPO uct="UPSTREAM_CONN" urt="UPSTREAM_RESP" "USER_AGENT"
# Exemplo:
# 172.18.0.1 [02/Mar/2026:19:38:28 +0000] "GET /api/estoque/produtos HTTP/1.1" 200 512 rt=0.006 uct="0.001" urt="0.007" "curl/8.5.0"
```

---

## Testar todos os requisitos

Um único script valida todos os requisitos (3.1 ao 4.3):

```bash
# Copiar e executar dentro do container nginx
docker cp test-requirements.sh caixaverso-nginx:/tmp/test-requirements.sh
docker exec caixaverso-nginx sh /tmp/test-requirements.sh
```

O script verifica:
- **3.1** Proxy reverso porta 80
- **3.2** Security headers obrigatórios
- **3.3** Rate limiting com retorno 429
- **3.4** Rejeição de payload > 1MB (413)
- **3.5** Compressão GZIP com `Content-Encoding: gzip`
- **3.6** Log estruturado com IP, método, status e tempos
- **4.1** Cache GET 10s (HIT/MISS) + bypass por Authorization
- **4.2** Basic Auth em `/api/actuator` (401 sem credenciais, 200 com)
- **4.3** HTTPS TLS 1.3 com certificado autoassinado

---

## Desenvolvimento local (sem Docker)

```bash
# Requer Java 21 e Maven 3.9
mvn clean install -DskipTests
mvn spring-boot:run

# Acesso direto (sem Nginx):
# API:        http://localhost:8080/api
# H2 Console: http://localhost:8080/api/h2-console
#   JDBC URL: jdbc:h2:file:./data/caixaverso;MODE=PostgreSQL
#   User: sa  /  Password: sa
```

---

## Estrutura de arquivos relevantes

```
caixaverso-introarq/
├── Dockerfile                    # Build multi-stage da aplicação Java
├── docker-compose.yml            # Orquestração de todos os serviços
├── test-requirements.sh          # Script de validação dos requisitos Nginx
├── nginx/
│   ├── Dockerfile                # Build do container Nginx com certs e htpasswd
│   └── nginx.conf                # Configuração completa do Nginx
└── src/main/resources/
    ├── application.yaml          # Perfil padrão (H2, porta 8080)
    └── application-prod.yaml     # Perfil produção (PostgreSQL, porta 8080)
```

---

**Versão**: 1.1.0  
**Atualizado**: 2026-03-02
