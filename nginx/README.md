# Guia Rápido - Nginx Setup

## 🚀 Início Rápido

### 1. Verificar Estrutura Criada

```bash
# Estrutura esperada:
# nginx/
# ├── nginx.conf          (Configuração principal - 500 linhas)
# ├── Dockerfile          (Build do container Nginx)
# └── CERTIFICADO.md      (Guia para certificados)

# docker-compose.yml      (Atualizado com Nginx)
```

### 2. Build e Deploy

```bash
# Build das imagens
docker-compose build

# Iniciar stack completa (Nginx + Minierp + DB + RabbitMQ)
docker-compose up -d

# Verificar containers em execução
docker-compose ps

# Ver logs em tempo real
docker-compose logs -f nginx
```

### 3. Testar Funcionalidade

```bash
# HTTP (redireciona para HTTPS)
curl -i http://localhost/

# HTTPS com certificado auto-assinado
curl -k https://localhost/health

# API com teste de rate limiting
for i in {1..20}; do 
  curl -s -w "Status: %{http_code}\n" -k https://localhost/api/vendas
done
# Esperado: Primeiras requisições com 200, após limite com 429
```

## 🔐 Mecanismos de Segurança Implementados

### SSL/TLS
- ✅ TLS 1.2 + 1.3
- ✅ Cipher suites modernas
- ✅ HSTS (força HTTPS)
- ✅ Session resumption

### Headers de Segurança
- ✅ X-Frame-Options (previne clickjacking)
- ✅ X-Content-Type-Options (previne MIME sniffing)
- ✅ Content-Security-Policy (previne XSS)
- ✅ X-XSS-Protection
- ✅ Referrer-Policy

### Rate Limiting
- ✅ 10 req/s geral
- ✅ 5 req/s para /api
- ✅ 2 req/s para operações de escrita
- ✅ 3 req/s para /login (brute force protection)

### Proteção contra Ataques
- ✅ Bloqueia arquivos ocultos (/.*)
- ✅ Bloqueia config files (.env, .git)
- ✅ IP whitelisting para /admin
- ✅ CORS validado

## ⚡ Otimizações de Performance

### Caching
- ✅ GET requests: 5 minutos em proxy
- ✅ Estáticos (JS/CSS): 30 dias no navegador
- ✅ Compressão gzip automática (~70% redução)

### Networking
- ✅ sendfile (zero-copy)
- ✅ keepalive (reutiliza conexões)
- ✅ tcp_nopush (agrupa dados)
- ✅ tcp_nodelay (reduz latência)

## 🌐 Roteamento de Endpoints

```
GET  /health          → Health check (sem logs)
POST /api/vendas      → Rate limit 2r/s, sem cache
GET  /api/vendas      → Rate limit 5r/s, cache 5min
GET  /static/*        → Cache agressivo 30 dias
POST /api/auth/login  → Rate limit 3r/s (brute force)
*    /admin/*         → IP whitelisting
```

## 📊 Monitoramento

### Ver Logs
```bash
# Acesso
docker exec caixaverso-nginx tail -f /var/log/nginx/access.log

# Segurança
docker exec caixaverso-nginx tail -f /var/log/nginx/security.log

# Erros
docker exec caixaverso-nginx tail -f /var/log/nginx/error.log
```

### Estatísticas de Rate Limiting
```bash
# IPs que atingiram limite
docker exec caixaverso-nginx grep "429" /var/log/nginx/access.log | wc -l

# IPs mais ativos
docker exec caixaverso-nginx awk '{print $1}' /var/log/nginx/access.log | sort | uniq -c | sort -rn | head
```

## 🔧 Configurar em Produção

### 1. Certificado Let's Encrypt
```bash
# Gerar certificado
docker-compose run --rm certbot certonly --standalone -d seu-dominio.com

# Copiar para nginx
cp /etc/letsencrypt/live/seu-dominio.com/fullchain.pem nginx/certs/server.crt
cp /etc/letsencrypt/live/seu-dominio.com/privkey.pem nginx/certs/server.key

# Reiniciar nginx
docker-compose restart nginx
```

### 2. Atualizar Configurações
```nginx
# nginx.conf
server_name seu-dominio.com;  # Substitua localhost

# SSL paths (se usando Let's Encrypt)
ssl_certificate /etc/nginx/certs/fullchain.pem;
ssl_certificate_key /etc/nginx/certs/privkey.pem;
```

### 3. Habilitar Renovação Automática
```bash
# Criar cron job
0 0 1 * * /usr/bin/certbot renew && docker exec caixaverso-nginx nginx -s reload
```

## ❌ Problemas Comuns

### 1. Erro 403 Forbidden
**Causa:** Pode ser rate limiting, CORS ou permission
```bash
# Verificar nos logs
docker logs caixaverso-nginx | grep "403"

# Se for rate limit, aguarde alguns minutos
# Se for CORS, verifique Origin header
```

### 2. Erro 429 Too Many Requests
**Causa:** Atingiu rate limit
```bash
# Esperado ao testar rate limiting
# Aguarde 10 segundos (limite se reseta)
# Ou mude para outro IP/cliente
```

### 3. Certificado Inválido
**Em Desenvolvimento:**
```bash
# Chrome: Clique "Avançado" → "Prosseguir mesmo assim"
# curl: Use -k (--insecure)
```

**Em Produção:**
```bash
# Use certificado válido do Let's Encrypt
# Será reconhecido automaticamente
```

### 4. Conexão Recusada
```bash
# Verificar se Nginx iniciou
docker ps | grep nginx

# Verificar logs
docker logs caixaverso-nginx

# Reiniciar
docker-compose restart nginx
```

## 📝 Checklist

- [ ] docker-compose.yml atualizado com Nginx
- [ ] nginx/nginx.conf criado com 500+ linhas de config
- [ ] nginx/Dockerfile criado (gera certificado auto-assinado)
- [ ] Certificados em nginx/certs/
- [ ] Stack iniciada: `docker-compose up -d`
- [ ] Health check passa: `curl -k https://localhost/health`
- [ ] Rate limiting funciona: `curl -k https://localhost/api/vendas` (x20)
- [ ] Logs sendo coletados
- [ ] CORS funcionando para seu frontend
- [ ] Documentação em docs/NGINX_IMPLEMENTATION.md

## 🎓 Próximos Passos

1. **WAF (Web Application Firewall):** ModSecurity com Nginx
2. **Metrics:** Prometheus + Grafana para monitorar
3. **Load Balancing:** Múltiplos backends em alta disponibilidade
4. **API Gateway:** Kong ou AWS API Gateway
5. **Observabilidade:** Jaeger/Zipkin para distributed tracing

---

**Versão:** 1.0  
**Última Atualização:** 28/01/2026
