FROM nginx:1.25-alpine

# Atualiza pacotes e instala ferramentas úteis
RUN apk add --no-cache \
    curl \
    openssl \
    apache2-utils \
    && rm -rf /var/cache/apk/*

# Remove configuração padrão
RUN rm -f /etc/nginx/conf.d/default.conf

# Copia configuração customizada
COPY nginx.conf /etc/nginx/nginx.conf

# Cria diretórios necessários
RUN mkdir -p /var/cache/nginx/cache \
    && mkdir -p /etc/nginx/certs

# ============================================================
# 4.3 Certificado autoassinado para HTTPS simulado (desenvolvimento)
# Em produção, substituir por Let's Encrypt ou certificado válido
# ============================================================
RUN openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
    -keyout /etc/nginx/certs/server.key \
    -out /etc/nginx/certs/server.crt \
    -subj "/C=BR/ST=SP/L=Sao Paulo/O=CaixaVerso/CN=localhost"

# ============================================================
# 4.2 Basic Auth para Swagger e Actuator
# Usuário: admin | Senha: admin123
# ============================================================
RUN htpasswd -cb /etc/nginx/.htpasswd admin admin123

# Permissões
RUN chmod 644 /etc/nginx/.htpasswd \
    && chmod -R 755 /var/cache/nginx \
    && chmod -R 600 /etc/nginx/certs

# Healthcheck
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
    CMD curl -f http://localhost/health || exit 1

# Expõe portas
EXPOSE 80 443

# Comando padrão
CMD ["nginx", "-g", "daemon off;"]
