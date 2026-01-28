package com.caixaverso.minierp.vendas.domain.service;

import com.caixaverso.minierp.vendas.domain.model.Venda;
import com.caixaverso.minierp.vendas.domain.repository.VendaRepository;
import com.caixaverso.minierp.vendas.domain.event.VendaConfirmadaEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * Serviço de Domínio: ConfirmarVendaService
 *
 * Confirma uma venda após sucessos de estoque e cobrança (SAGA).
 */
@Service
@AllArgsConstructor
public class ConfirmarVendaService {

    private final VendaRepository vendaRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Confirma uma venda
     */
    public void executar(Long vendaId) {
        Venda venda = vendaRepository.findById(vendaId)
            .orElseThrow(() -> new IllegalArgumentException("Venda não encontrada: " + vendaId));

        // Confirmar venda (valida estado)
        venda.confirmar();

        // Persistir mudança
        vendaRepository.save(venda);

        // Publicar evento
        VendaConfirmadaEvent event = new VendaConfirmadaEvent(
            venda.getId(),
            venda.getClienteId(),
            venda.getValorTotal()
        );
        eventPublisher.publishEvent(event);
    }
}

