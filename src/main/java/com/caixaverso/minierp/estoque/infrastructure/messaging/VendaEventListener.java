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
            log.info("📦 Reservando estoque para venda: {} - Produto: {} - Quantidade: {}",
                event.getVendaId(), event.getProdutoId(), event.getQuantidade());

            // Reservar estoque através do Application Service
            estoqueApplicationService.reservarEstoque(
                event.getVendaId(),
                event.getProdutoId(),
                event.getQuantidade()
            );

            log.info("✅ Estoque reservado com sucesso para venda: {}", event.getVendaId());

        } catch (Exception e) {
            log.error("❌ Erro ao reservar estoque para venda: {}", event.getVendaId(), e);
            throw e;
        }
    }
}

