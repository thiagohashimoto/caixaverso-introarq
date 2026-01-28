package com.caixaverso.minierp.financeiro.domain.model;

import lombok.Value;

/**
 * Value Object: CobrancaId
 */
@Value
public class CobrancaId {
    Long value;

    public CobrancaId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("CobrancaId deve ser maior que zero");
        }
        this.value = value;
    }
}

