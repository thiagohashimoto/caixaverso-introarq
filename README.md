# CaixaVerso - Mini ERP Platform

Uma plataforma de gerenciamento empresarial modular e escalável desenvolvida com arquitetura de microsserviços, implementando padrões enterprise-grade para vendas, estoque e gestão financeira.

## 📌 Visão Geral

MiniERP é um sistema integrado que gerencia o fluxo completo de vendas, controle de inventário e processamento financeiro. A arquitetura foi projetada para permitir evolução futura para microsserviços com manutenção mínima de código.

**Componentes principais:**
- **Vendas**: Criação e gestão de pedidos
- **Estoque**: Controle de inventário em tempo real
- **Financeiro**: Processamento de cobranças e pagamentos

## 🏗️ Arquitetura

### Padrão Arquitetural

A plataforma segue uma **arquitetura monolítica modular** com estrutura em camadas, permitindo fácil transição para microsserviços:

```
┌─────────────────────────────────────────────┐
│         REST API Layer (Controllers)        │
├─────────────────────────────────────────────┤
│     Application Services & DTOs Layer       │
├─────────────────────────────────────────────┤
│     Domain Layer (Business Logic)           │
├─────────────────────────────────────────────┤
│   Infrastructure (Persistence, Events)     │
└─────────────────────────────────────────────┘
```

### Estrutura de Módulos

Cada módulo funciona de forma independente com comunicação via eventos de domínio:

```
src/main/java/com/caixaverso/minierp/
├── vendas/                           # Módulo de Vendas
│   ├── domain/                       # Lógica de negócio pura
│   │   ├── model/                   # Entidades: Venda, ItemVenda
│   │   ├── event/                   # Eventos: VendaCriadaEvent
│   │   ├── service/                 # Serviços de domínio
│   │   └── repository/              # Interfaces de repositório
│   │
│   ├── application/                 # Orquestração de casos de uso
│   │   ├── dto/                     # CriarVendaDTO, VendaResponseDTO
│   │   ├── service/                 # VendaApplicationService
│   │   └── mapper/                  # Mapeadores (DTO ↔ Domain)
│   │
│   ├── infrastructure/              # Detalhes técnicos
│   │   ├── persistence/             # VendaJpaRepository
│   │   ├── messaging/               # Event listeners
│   │   └── config/                  # Configurações do módulo
│   │
│   └── api/                         # Interface REST
│       ├── controller/              # VendaController
│       └── exception/               # Tratamento de erros
│
├── estoque/                          # Módulo de Estoque (estrutura similar)
├── financeiro/                       # Módulo de Financeiro (estrutura similar)
├── shared/                           # Código compartilhado
│   ├── domain/                       # Value Objects comuns
│   ├── api/
│   │   └── gateway/                 # Simula API Gateway
│   └── infrastructure/
│       ├── config/                  # Configurações globais
│       └── messaging/               # Event bus centralizado
│
└── common/                           # Utilidades gerais
    ├── annotation/                  # Anotações customizadas
    └── constant/                    # Constantes da aplicação
```

## 🔄 Fluxo de Processamento

A plataforma coordena transações distribuídas:

```
1. Cliente: POST /api/vendas
   └─ VendaController → VendaApplicationService

2. Venda criada no estado PENDENTE
   └─ Publica: VendaCriadaEvent ✓

3. EstoqueEventListener escuta VendaCriadaEvent
   ├─ Reserva quantidade em estoque
   ├─ Se sucesso: publica EstoqueReservadoEvent
   └─ Se erro: publica VendaCancelada (compensação)

4. FinanceiroEventListener escuta EstoqueReservadoEvent
   ├─ Cria cobrança associada
   ├─ Se sucesso: publica CobrancaCriadaEvent
   └─ Se erro: libera estoque e cancela venda

5. VendaEventListener escuta CobrancaCriadaEvent
   ├─ Transição: PENDENTE → CONFIRMADA
   └─ Publica: VendaConfirmadaEvent

Resultado: Venda completa com estoque reservado e cobrança criada
```

### Eventos de Domínio

O sistema utiliza eventos para comunicação assíncrona entre módulos:

| Evento | Fonte | Destino | Ação |
|--------|-------|---------|------|
| `VendaCriadaEvent` | Vendas | Estoque, Financeiro | Iniciar SAGA |
| `EstoqueReservadoEvent` | Estoque | Financeiro | Criar cobrança |
| `CobrancaCriadaEvent` | Financeiro | Vendas | Confirmar venda |
| `VendaCanceladaEvent` | Estoque/Financeiro | Vendas | Reverter estado |

## 🛠️ Stack Tecnológico

### Framework & Linguagem
- **Spring Boot 3.2.0** - Framework principal
- **Java 21** - Linguagem de programação
- **Spring Web** - REST Controllers
- **Spring Data JPA** - Acesso a dados com ORM

### Persistência
- **H2 Database** - Banco de dados file-based (desenvolvimento)
- **PostgreSQL** - Compatibilidade produção (opcional)
- **Hibernate** - Mapeamento objeto-relacional

### Padrões & Boas Práticas
- **Spring Security 6.x** - Configuração de segurança
- **Spring Events** - Pub/Sub de eventos (SAGA Pattern)
- **Lombok** - Redução de boilerplate
- **MapStruct** - Mapeamento type-safe de DTOs

### Testes
- **JUnit 5** - Framework de testes
- **Mockito** - Mocking e spying
- **Spring Boot Test** - Testes de integração
- **AssertJ** - Assertions fluentes

### Build & Dependências
- **Maven 3.6+** - Gerenciador de dependências
- **Maven Compiler** - Compilação Java 21

## 🚀 Como Começar

### Pré-requisitos

```
- Java 21+
- Maven 3.6+
- Git
```

### Instalação

1. **Clone o repositório**
```bash
git clone https://github.com/seu-usuario/caixaverso.git
cd caixaverso
```

2. **Compile o projeto**
```bash
mvn clean install
```

3. **Execute a aplicação**
```bash
mvn spring-boot:run
```

A aplicação inicia em `http://localhost:8080`

### Acessar H2 Console

Banco de dados em-memória para desenvolvimento:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:file:./data/caixaverso;MODE=PostgreSQL`
- Username: `sa`
- Password: `sa`

## 📡 API REST

### Vendas

**Criar Venda**
```http
POST /api/vendas
Content-Type: application/json

{
  "clienteId": 1,
  "itens": [
    {
      "produtoId": 10,
      "quantidade": 5,
      "valor": 99.99
    }
  ]
}

Response: 201 Created
{
  "id": 1,
  "clienteId": 1,
  "status": "PENDENTE",
  "total": 499.95,
  "dataCriacao": "2026-01-28T10:30:00"
}
```

**Obter Venda**
```http
GET /api/vendas/1

Response: 200 OK
{
  "id": 1,
  "clienteId": 1,
  "status": "CONFIRMADA",
  "itens": [...],
  "total": 499.95
}
```

**Listar Vendas**
```http
GET /api/vendas

Response: 200 OK
[
  { "id": 1, "status": "CONFIRMADA", ... },
  { "id": 2, "status": "PENDENTE", ... }
]
```

**Confirmar Venda**
```http
PUT /api/vendas/1/confirmar

Response: 200 OK
{ "id": 1, "status": "CONFIRMADA" }
```

### Estoque

**Listar Produtos**
```http
GET /api/estoque

Response: 200 OK
[
  {
    "produtoId": 10,
    "nome": "Produto A",
    "quantidade": 100,
    "valor": 99.99
  }
]
```

**Obter Estoque de Produto**
```http
GET /api/estoque/10

Response: 200 OK
{
  "produtoId": 10,
  "quantidade": 95
}
```

**Atualizar Estoque**
```http
PUT /api/estoque/10
Content-Type: application/json

{
  "quantidade": 50
}

Response: 200 OK
```

### Financeiro

**Listar Cobranças**
```http
GET /api/financeiro/cobrancas

Response: 200 OK
[
  {
    "id": 1,
    "vendaId": 1,
    "valor": 499.95,
    "status": "ABERTA"
  }
]
```

**Registrar Pagamento**
```http
PUT /api/financeiro/cobrancas/1/pagar

Response: 200 OK
{
  "id": 1,
  "status": "PAGA"
}
```

## 📊 Diagrama de Entidades

## 🔐 Segurança

### Spring Security

A aplicação utiliza Spring Security 6.x com configuração customizada:

- H2 Console acessível via `/h2-console`
- CSRF desabilitado para endpoints específicos
- Frames permitidos para console H2
- Preparado para autenticação JWT futura

### Configuração de Segurança

```java
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/api/**").permitAll()
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**")
            )
            .headers(headers -> headers
                .frameOptions(frameOptions -> 
                    frameOptions.disable()
                )
            );
        return http.build();
    }
}
```

## 🔍 Monitoramento & Logs

Logs estruturados para debugging e monitoramento:

```yaml
logging:
  level:
    com.caixaverso.minierp: DEBUG
    org.springframework: INFO
  pattern:
    console: "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
```

## 🐛 Troubleshooting

### Erro: "Forbidden" ao acessar /api/vendas

**Solução**: Verifique se Spring Security está configurado corretamente em `WebSecurityConfig.java`

```java
.requestMatchers("/api/**").permitAll()
```

### Erro: H2 Console "403 Access Denied"

**Solução**: Configure header X-Frame-Options em WebSecurityConfig

```java
.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
```

### Erro: "Connection refused" ao conectar banco de dados

**Solução**: Verifique a URL de conexão em `application.yaml`

```yaml
datasource:
  url: jdbc:h2:file:./data/caixaverso;MODE=PostgreSQL
  username: sa
  password: sa
```

## 📝 Convenções de Código

### Nomenclatura

- **Entidades**: `Venda`, `Produto`, `Cliente`
- **Value Objects**: `VendaId`, `Email`, `Valor`
- **Services (Domain)**: `CriarVendaService`
- **Services (Application)**: `VendaApplicationService`
- **Controllers**: `VendaController`
- **DTOs**: `CriarVendaDTO`, `VendaResponseDTO`
- **Repositories**: `VendaRepository` (interface)
- **Events**: `VendaCriadaEvent`
- **Mappers**: `VendaMapper`

## 🤝 Contribuindo

### Checklist de Feature

- [ ] Entidade/Value Object criados em `domain/model`
- [ ] DTO criado em `application/dto`
- [ ] Mapper criado em `application/mapper`
- [ ] Application Service implementado
- [ ] Repository interface em `domain/repository`
- [ ] Repository JPA implementado
- [ ] Controller REST implementado
- [ ] Testes unitários implementados
- [ ] Testes de integração implementados
- [ ] Eventos de domínio publicados (se necessário)
- [ ] Documentação atualizada

---

**Versão**: 1.0.0  
**Última atualização**: 2026-01-28  
**Status**: Production-Ready
