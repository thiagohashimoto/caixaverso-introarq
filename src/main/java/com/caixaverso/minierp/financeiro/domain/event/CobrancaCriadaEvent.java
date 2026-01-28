package com.caixaverso.minierp.financeiro.domain.event;

import com.caixaverso.minierp.shared.domain.event.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Evento de Domínio: CobrancaCriadaEvent
 *
 * Publicado quando uma cobrança é criada com sucesso.
 * Sinaliza para o módulo Vendas confirmar a venda (final da SAGA).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CobrancaCriadaEvent implements DomainEvent {

    private String eventId;
    private Long cobrancaId;
    private Long vendaId;
    private Long clienteId;
    private BigDecimal valor;
    private LocalDateTime ocorridoEm;

    public CobrancaCriadaEvent(Long cobrancaId, Long vendaId, Long clienteId, BigDecimal valor) {
        this.eventId = UUID.randomUUID().toString();
        this.cobrancaId = cobrancaId;
        this.vendaId = vendaId;
        this.clienteId = clienteId;
        this.valor = valor;
        this.ocorridoEm = LocalDateTime.now();
    }

    @Override
    public String getEventId() {
        return eventId;
    }

    @Override
    public Object getAggregateId() {
        return vendaId;
    }

    @Override
    public LocalDateTime getOccurredAt() {
        return ocorridoEm;
    }

    @Override
    public String getEventName() {
        return "CobrancaCriada";
    }
}

