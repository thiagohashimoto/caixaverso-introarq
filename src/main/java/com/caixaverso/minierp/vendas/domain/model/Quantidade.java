package com.caixaverso.minierp.vendas.domain.model;

import lombok.Value;

/**
 * Value Object: Quantidade
 *
 * Encapsula validações de quantidade de produtos.
 */
@Value
public class Quantidade {
    Integer value;

    public Quantidade(Integer value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        this.value = value;
    }

    public static Quantidade of(Integer value) {
        return new Quantidade(value);
    }

    public Quantidade somar(Quantidade outra) {
        return new Quantidade(this.value + outra.value);
    }

    public Quantidade subtrair(Quantidade outra) {
        int resultado = this.value - outra.value;
        if (resultado < 0) {
            throw new IllegalArgumentException("Quantidade insuficiente");
        }
        return new Quantidade(resultado);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

