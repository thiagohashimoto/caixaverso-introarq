package com.caixaverso.minierp.estoque.domain.model;

import lombok.Value;

/**
 * Value Object: ProdutoId
 */
@Value
public class ProdutoId {
    Long value;

    public ProdutoId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("ProdutoId deve ser maior que zero");
        }
        this.value = value;
    }
}

