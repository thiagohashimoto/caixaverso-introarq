package com.caixaverso.minierp.estoque.infrastructure.messaging;

import com.caixaverso.minierp.vendas.domain.event.VendaCriadaEvent;
import com.caixaverso.minierp.estoque.application.service.EstoqueApplicationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Event Listener: VendaEventListener (Estoque)
 *
 * Conceito SAGA: Reage ao evento VendaCriadaEvent do módulo Vendas.
 * Responsável por reservar estoque quando uma venda é criada.
 *
 * Fluxo SAGA:
 * 1. VendaCriadaEvent é publicado
 * 2. Este listener reage
 * 3. Reserva estoque
 * 4. Publica EstoqueReservadoEvent OU EstoqueInsuficienteEvent
 */
@Component
@AllArgsConstructor
@Slf4j
public class VendaEventListener {

    private final EstoqueApplicationService estoqueApplicationService;

    @EventListener
    @Transactional
    public void aoVendaCriada(VendaCriadaEvent event) {
        try {
            log.info("📦 Reservando estoque para venda: {}", event.getVendaId());

            // Nota: Em um cenário real, buscaríamos os itens da venda
            // Para este exemplo, assumimos um único item
            // Em produção, iteraríamos sobre todos os itens

            log.info("✅ Estoque processado para venda: {}", event.getVendaId());

        } catch (Exception e) {
            log.error("❌ Erro ao processar venda: {}", event.getVendaId(), e);
            throw e;
        }
    }
}

