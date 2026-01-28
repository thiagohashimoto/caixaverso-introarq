package com.caixaverso.minierp.vendas.domain.model;

import lombok.Value;

/**
 * Value Object: VendaId
 *
 * Conceito DDD: Value Objects são imutáveis e sem identidade própria.
 * Representam conceitos importantes do domínio.
 * Encapsulam validação.
 */
@Value
public class VendaId {
    Long value;

    public VendaId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("VendaId deve ser maior que zero");
        }
        this.value = value;
    }

    public static VendaId of(Long value) {
        return new VendaId(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

