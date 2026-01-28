package com.caixaverso.minierp.vendas.domain.event;

import com.caixaverso.minierp.shared.domain.event.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Evento de Domínio: VendaCanceladaEvent
 *
 * Publicado quando a venda é cancelada.
 * Dispara compensações: libera estoque e cancela cobrança.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendaCanceladaEvent implements DomainEvent {

    private String eventId;
    private Long vendaId;
    private Long clienteId;
    private String motivo;
    private LocalDateTime ocorridoEm;

    public VendaCanceladaEvent(Long vendaId, Long clienteId, String motivo) {
        this.eventId = UUID.randomUUID().toString();
        this.vendaId = vendaId;
        this.clienteId = clienteId;
        this.motivo = motivo;
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
        return "VendaCancelada";
    }
}

