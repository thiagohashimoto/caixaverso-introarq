package com.caixaverso.minierp.estoque.domain.event;

import com.caixaverso.minierp.shared.domain.event.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Evento de Domínio: EstoqueInsuficienteEvent
 *
 * Publicado quando não há estoque disponível para reserva.
 * Dispara compensação: cancela venda e libera recursos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstoqueInsuficienteEvent implements DomainEvent {

    private String eventId;
    private Long vendaId;
    private Long produtoId;
    private Integer quantidadeSolicitada;
    private Integer quantidadeDisponivel;
    private LocalDateTime ocorridoEm;

    public EstoqueInsuficienteEvent(Long vendaId, Long produtoId,
                                   Integer quantidadeSolicitada, Integer quantidadeDisponivel) {
        this.eventId = UUID.randomUUID().toString();
        this.vendaId = vendaId;
        this.produtoId = produtoId;
        this.quantidadeSolicitada = quantidadeSolicitada;
        this.quantidadeDisponivel = quantidadeDisponivel;
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
        return "EstoqueInsuficiente";
    }
}

