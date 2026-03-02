package com.caixaverso.minierp.vendas.infrastructure.messaging;

import com.caixaverso.minierp.financeiro.domain.event.CobrancaCriadaEvent;
import com.caixaverso.minierp.vendas.domain.service.ConfirmarVendaService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Event Listener: CobrancaEventListener (Vendas)
 *
 * Conceito SAGA: Reage ao evento CobrancaCriadaEvent do módulo Financeiro.
 * Responsável por confirmar a venda quando a cobrança é criada com sucesso.
 *
 * Fluxo SAGA:
 * 1. VendaCriadaEvent é publicado (Vendas)
 * 2. EstoqueEventListener reserva estoque (Estoque)
 * 3. EstoqueReservadoEvent é publicado (Estoque)
 * 4. EstoqueEventListener (Financeiro) cria cobrança (Financeiro)
 * 5. CobrancaCriadaEvent é publicado (Financeiro)
 * 6. Este listener reage (Vendas) ← VOCÊ ESTÁ AQUI
 * 7. Confirma a venda (CONFIRMADA) ✅
 * 8. Publica VendaConfirmadaEvent (fim da SAGA)
 */
@Component
@AllArgsConstructor
@Slf4j
public class CobrancaEventListener {

    private final ConfirmarVendaService confirmarVendaService;

    /**
     * Reage ao evento CobrancaCriadaEvent
     *
     * Quando a cobrança é criada com sucesso, significa que:
     * - ✅ Venda foi criada
     * - ✅ Estoque foi reservado
     * - ✅ Cobrança foi criada
     *
     * Logo, a venda deve ser confirmada (final da SAGA)
     *
     * @param event Evento de cobrança criada com informações da venda
     */
    @EventListener
    @Transactional
    public void aoCobrancaCriada(CobrancaCriadaEvent event) {
        try {
            log.info("💰 Confirmando venda: {} - Cobrança: {} - Valor: {}",
                event.getVendaId(), event.getCobrancaId(), event.getValor());

            // Confirmar a venda após sucesso de estoque e cobrança
            confirmarVendaService.executar(event.getVendaId());

            log.info("✅ Venda confirmada com sucesso: {} (SAGA completa!)", event.getVendaId());

        } catch (Exception e) {
            log.error("❌ Erro ao confirmar venda: {}", event.getVendaId(), e);
            throw e;
        }
    }
}
