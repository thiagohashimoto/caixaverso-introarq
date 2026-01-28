package com.caixaverso.minierp.estoque.domain.event;

import com.caixaverso.minierp.shared.domain.event.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Evento de Domínio: EstoqueReservadoEvent
 *
 * Publicado quando estoque é reservado com sucesso.
 * Dispara a criação de cobrança no módulo Financeiro.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstoqueReservadoEvent implements DomainEvent {

    private String eventId;
    private Long vendaId;
    private Long produtoId;
    private Integer quantidade;
    private LocalDateTime ocorridoEm;

    public EstoqueReservadoEvent(Long vendaId, Long produtoId, Integer quantidade) {
        this.eventId = UUID.randomUUID().toString();
        this.vendaId = vendaId;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
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
        return "EstoqueReservado";
    }
}

