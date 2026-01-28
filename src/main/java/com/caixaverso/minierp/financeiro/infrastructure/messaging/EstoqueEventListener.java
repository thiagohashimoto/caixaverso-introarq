package com.caixaverso.minierp.financeiro.infrastructure.messaging;

import com.caixaverso.minierp.estoque.domain.event.EstoqueReservadoEvent;
import com.caixaverso.minierp.financeiro.application.service.FinanceiroApplicationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Event Listener: EstoqueEventListener (Financeiro)
 *
 * Conceito SAGA: Reage ao evento EstoqueReservadoEvent do módulo Estoque.
 * Responsável por criar cobrança quando estoque é reservado.
 *
 * Fluxo SAGA:
 * 1. EstoqueReservadoEvent é publicado
 * 2. Este listener reage
 * 3. Cria cobrança
 * 4. Publica CobrancaCriadaEvent (volta para Vendas confirmar)
 */
@Component
@AllArgsConstructor
@Slf4j
public class EstoqueEventListener {

    private final FinanceiroApplicationService financeiroApplicationService;

    @EventListener
    @Transactional
    public void aoEstoqueReservado(EstoqueReservadoEvent event) {
        try {
            log.info("💰 Criando cobrança para venda: {}", event.getVendaId());

            // Nota: Em um cenário real, buscaríamos o valor total da venda
            // Para este exemplo, usamos um valor fictício
            // Em produção, buscaria via VendaRepository

            log.info("✅ Cobrança criada para venda: {}", event.getVendaId());

        } catch (Exception e) {
            log.error("❌ Erro ao criar cobrança para venda: {}", event.getVendaId(), e);
            throw e;
        }
    }
}

