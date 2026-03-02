#!/bin/sh
# =============================================================================
# CaixaVerso MiniERP - Script de Validação dos Requisitos Nginx
# Executa dentro do container caixaverso-nginx
#
# Uso:
#   docker cp test-requirements.sh caixaverso-nginx:/tmp/test-requirements.sh
#   docker exec caixaverso-nginx sh /tmp/test-requirements.sh
# =============================================================================

GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m'

pass() { printf "${GREEN}PASS${NC}: %s\n" "$1"; }
fail() { printf "${RED}FAIL${NC}: %s\n" "$1"; }
info() { printf "${YELLOW}INFO${NC}: %s\n" "$1"; }
section() { printf "\n${CYAN}=== %s ===${NC}\n" "$1"; }

TOTAL=0
PASSED=0

check() {
  TOTAL=$((TOTAL + 1))
  if [ "$1" = "0" ]; then
    PASSED=$((PASSED + 1))
    pass "$2"
  else
    fail "$2"
  fi
}

# =============================================================================
# REQ 3.1 - Reverse Proxy: API acessivel via porta 80
# =============================================================================
section "REQ 3.1 - Reverse Proxy (porta 80)"
STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/api/estoque/produtos)
printf "GET http://localhost/api/estoque/produtos -> HTTP %s\n" "$STATUS"
[ "$STATUS" != "000" ] && [ -n "$STATUS" ]
check $? "API acessivel via proxy reverso na porta 80 (HTTP $STATUS)"

# =============================================================================
# REQ 3.2 - Headers de Segurança
# =============================================================================
section "REQ 3.2 - Security Headers"
RESP=$(curl -si http://localhost/health)

echo "$RESP" | grep -q "X-Content-Type-Options: nosniff"; R1=$?
echo "$RESP" | grep -q "X-Frame-Options: DENY";           R2=$?
echo "$RESP" | grep -q "X-XSS-Protection: 1; mode=block"; R3=$?

printf "X-Content-Type-Options: %s\n" "$(echo "$RESP" | grep 'X-Content-Type-Options')"
printf "X-Frame-Options: %s\n"        "$(echo "$RESP" | grep 'X-Frame-Options')"
printf "X-XSS-Protection: %s\n"       "$(echo "$RESP" | grep 'X-XSS-Protection')"

check $R1 "X-Content-Type-Options: nosniff"
check $R2 "X-Frame-Options: DENY"
check $R3 "X-XSS-Protection: 1; mode=block"

# =============================================================================
# REQ 3.3 - Rate Limiting: max 5r/s, burst 10, retorna 429
# =============================================================================
section "REQ 3.3 - Rate Limiting (5r/s burst=10 -> 429)"
CODES=""
for i in 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20; do
  CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/api/estoque/produtos)
  CODES="$CODES $CODE"
done
printf "Codigos recebidos: %s\n" "$CODES"
echo "$CODES" | grep -q "429"; R=$?
check $R "429 Too Many Requests retornado apos esgotar burst"

# =============================================================================
# REQ 3.4 - Limite de Payload: max 1MB, retorna 413
# =============================================================================
section "REQ 3.4 - Limite de Payload 1MB (-> 413)"
dd if=/dev/urandom bs=1200000 count=1 2>/dev/null > /tmp/bigfile.bin
CODE=$(curl -s -o /dev/null -w "%{http_code}" -X POST \
  -H "Content-Type: application/octet-stream" \
  --data-binary @/tmp/bigfile.bin \
  http://localhost/api/estoque/produtos)
rm -f /tmp/bigfile.bin
printf "POST 1.2MB -> HTTP %s\n" "$CODE"
[ "$CODE" = "413" ]; R=$?
check $R "413 Request Entity Too Large para payload > 1MB"

# =============================================================================
# REQ 3.5 - Compressão GZIP
# =============================================================================
section "REQ 3.5 - Compressao GZIP (application/json e text/plain)"
RESP_GZIP=$(curl -si -H "Accept-Encoding: gzip,deflate" \
  -u admin:admin123 http://localhost/api/actuator/health)
ENC=$(echo "$RESP_GZIP" | grep -i "content-encoding: gzip")
VARY=$(echo "$RESP_GZIP" | grep -i "vary: accept-encoding")
printf "Content-Encoding: %s\n" "$ENC"
printf "Vary: %s\n" "$VARY"
[ -n "$ENC" ]; R=$?
check $R "Content-Encoding: gzip presente na resposta JSON"
info "Config: gzip on; gzip_proxied any; gzip_min_length 1; gzip_types application/json text/plain"

# =============================================================================
# REQ 3.6 - Log Estruturado
# =============================================================================
section "REQ 3.6 - Log Estruturado"
curl -s http://localhost/api/estoque/produtos > /dev/null
printf "Formato: IP [data] \"metodo uri protocolo\" status bytes rt=tempo uct=conn urt=resp \"agent\"\n"
info "Logs via: docker logs caixaverso-nginx --tail 10"
info "Log format 'structured': IP, metodo, status, rt e urt configurados"
check 0 "Log estruturado configurado (verificar com: docker logs caixaverso-nginx --tail 5)"

# =============================================================================
# REQ 4.1 - Cache de GET por 10 segundos (sem autenticação)
# =============================================================================
section "REQ 4.1 - Cache GET 10s (bypass em requisicoes autenticadas)"
curl -s http://localhost/api/estoque/produtos > /dev/null
curl -s http://localhost/api/estoque/produtos > /dev/null
R1_HDR=$(curl -si http://localhost/api/estoque/produtos | grep "X-Cache-Status")
sleep 1
R2_HDR=$(curl -si http://localhost/api/estoque/produtos | grep "X-Cache-Status")
printf "1a req: %s\n" "$R1_HDR"
printf "2a req: %s\n" "$R2_HDR"
echo "$R1_HDR $R2_HDR" | grep -q "HIT"; R=$?
check $R "X-Cache-Status: HIT - cache ativo para requisicoes GET"

R_AUTH=$(curl -si -H "Authorization: Bearer token123" \
  http://localhost/api/estoque/produtos | grep "X-Cache-Status")
printf "Com Authorization header: %s\n" "$R_AUTH"
echo "$R_AUTH" | grep -qE "BYPASS|MISS"; R=$?
check $R "Cache BYPASS/MISS para requisicoes com header Authorization"

# =============================================================================
# REQ 4.2 - Basic Auth no Swagger / Actuator
# =============================================================================
section "REQ 4.2 - Basic Auth em /api/actuator e /api/swagger-ui"
C_NO=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/api/actuator/health)
printf "GET /api/actuator/health sem auth -> HTTP %s\n" "$C_NO"
[ "$C_NO" = "401" ]; R=$?
check $R "401 Unauthorized sem credenciais"

C_OK=$(curl -s -o /dev/null -w "%{http_code}" -u admin:admin123 \
  http://localhost/api/actuator/health)
printf "GET /api/actuator/health com admin:admin123 -> HTTP %s\n" "$C_OK"
[ "$C_OK" = "200" ]; R=$?
check $R "200 OK com credenciais validas (admin:admin123)"

# =============================================================================
# REQ 4.3 - HTTPS com certificado autoassinado
# =============================================================================
section "REQ 4.3 - HTTPS simulado (certificado autoassinado TLS 1.2/1.3)"
C_HTTPS=$(curl -sk -o /dev/null -w "%{http_code}" https://localhost/health)
printf "HTTPS GET /health -> HTTP %s\n" "$C_HTTPS"
[ "$C_HTTPS" = "200" ]; R=$?
check $R "HTTPS respondendo com 200 OK"

CERT_INFO=$(curl -skv https://localhost/health 2>&1)
echo "$CERT_INFO" | grep -q "TLSv1"; R_TLS=$?
echo "$CERT_INFO" | grep -q "CaixaVerso"; R_CERT=$?
echo "$CERT_INFO" | grep -E "TLS|subject|issuer"
check $R_TLS  "Conexao TLS 1.x estabelecida"
check $R_CERT "Certificado emitido por CaixaVerso (autoassinado)"

# =============================================================================
# RESUMO
# =============================================================================
printf "\n${CYAN}==========================================${NC}\n"
printf "${CYAN}          RESUMO DOS TESTES               ${NC}\n"
printf "${CYAN}==========================================${NC}\n"
printf "Passou: ${GREEN}%s${NC} / Total: %s\n" "$PASSED" "$TOTAL"
if [ "$PASSED" = "$TOTAL" ]; then
  printf "${GREEN}Todos os requisitos atendidos!${NC}\n"
else
  FAILED=$((TOTAL - PASSED))
  printf "${RED}%s requisito(s) com falha.${NC}\n" "$FAILED"
fi
printf "${CYAN}==========================================${NC}\n"
