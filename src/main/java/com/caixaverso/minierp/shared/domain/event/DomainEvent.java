package com.caixaverso.minierp.shared.domain.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Interface base para todos os eventos de domínio.
 *
 * Conceito DDD: Domain Events são notificações de coisas importantes
 * que aconteceram no domínio do negócio.
 */
public interface DomainEvent extends Serializable {

    /**
     * Identificador único do evento
     */
    String getEventId();

    /**
     * Agregado que originou o evento
     */
    Object getAggregateId();

    /**
     * Momento em que o evento ocorreu
     */
    LocalDateTime getOccurredAt();

    /**
     * Nome do evento para tracking
     */
    String getEventName();
}

