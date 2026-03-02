package com.caixaverso.minierp.financeiro.infrastructure.messaging;

import com.caixaverso.minierp.estoque.domain.event.EstoqueReservadoEvent;
import com.caixaverso.minierp.financeiro.application.service.FinanceiroApplicationService;
import com.caixaverso.minierp.vendas.domain.repository.VendaRepository;
import com.caixaverso.minierp.vendas.domain.model.Venda;
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
    private final VendaRepository vendaRepository;

    @EventListener
    @Transactional
    public void aoEstoqueReservado(EstoqueReservadoEvent event) {
        try {
            log.info("💰 Criando cobrança para venda: {} - Produto: {} - Quantidade: {}",
                event.getVendaId(), event.getProdutoId(), event.getQuantidade());

            // Buscar venda para obter clienteId e valor total
            Venda venda = vendaRepository.findById(event.getVendaId())
                .orElseThrow(() -> new IllegalArgumentException(
                    "Venda não encontrada para ID: " + event.getVendaId()
                ));

            // Criar cobrança através do Application Service
            financeiroApplicationService.criarCobranca(
                event.getVendaId(),
                venda.getClienteId(),
                venda.getValorTotal()
            );

            log.info("✅ Cobrança criada com sucesso para venda: {}", event.getVendaId());

        } catch (Exception e) {
            log.error("❌ Erro ao criar cobrança para venda: {}", event.getVendaId(), e);
            throw e;
        }
    }
}

