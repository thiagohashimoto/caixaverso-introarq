package com.caixaverso.minierp.vendas.domain.event;

import com.caixaverso.minierp.shared.domain.event.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Evento de Domínio: VendaConfirmadaEvent
 *
 * Publicado quando a venda é confirmada com sucesso.
 * Indica que estoque foi reservado e cobrança foi criada.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendaConfirmadaEvent implements DomainEvent {

    private String eventId;
    private Long vendaId;
    private Long clienteId;
    private java.math.BigDecimal valorTotal;
    private LocalDateTime ocorridoEm;

    public VendaConfirmadaEvent(Long vendaId, Long clienteId, java.math.BigDecimal valorTotal) {
        this.eventId = UUID.randomUUID().toString();
        this.vendaId = vendaId;
        this.clienteId = clienteId;
        this.valorTotal = valorTotal;
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
        return "VendaConfirmada";
    }
}

