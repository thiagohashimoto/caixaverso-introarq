package com.caixaverso.minierp.vendas.domain.event;

import com.caixaverso.minierp.shared.domain.event.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Evento de Domínio: VendaCriadaEvent
 *
 * Conceito DDD: Eventos de domínio notificam que algo importante
 * aconteceu no domínio. Outros módulos reagem a este evento.
 *
 * Conceito SAGA: Este evento dispara a cadeia de compensações
 * entre Vendas → Estoque → Financeiro
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendaCriadaEvent implements DomainEvent {

    private String eventId;
    private Long vendaId;
    private Long clienteId;
    private Long produtoId;
    private Integer quantidade;
    private java.math.BigDecimal valorTotal;
    private LocalDateTime ocorridoEm;

    public VendaCriadaEvent(Long vendaId, Long clienteId, Long produtoId, Integer quantidade, java.math.BigDecimal valorTotal) {
        this.eventId = UUID.randomUUID().toString();
        this.vendaId = vendaId;
        this.clienteId = clienteId;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
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
        return "VendaCriada";
    }
}

