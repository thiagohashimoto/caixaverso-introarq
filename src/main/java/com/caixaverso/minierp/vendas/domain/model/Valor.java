package com.caixaverso.minierp.vendas.domain.model;

import lombok.Value;
import java.math.BigDecimal;

/**
 * Value Object: Valor (Money Pattern)
 *
 * Encapsula validações de valor monetário.
 * Imutável e seguro contra operações inválidas.
 */
@Value
public class Valor {
    BigDecimal amount;
    String currency;

    public Valor(BigDecimal amount, String currency) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor não pode ser negativo");
        }
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Moeda é obrigatória");
        }
        this.amount = amount;
        this.currency = currency;
    }

    public static Valor of(BigDecimal amount, String currency) {
        return new Valor(amount, currency);
    }

    public static Valor zero(String currency) {
        return new Valor(BigDecimal.ZERO, currency);
    }

    public Valor somar(Valor outro) {
        if (!this.currency.equals(outro.currency)) {
            throw new IllegalArgumentException("Não é possível somar moedas diferentes");
        }
        return new Valor(this.amount.add(outro.amount), this.currency);
    }

    public Valor subtrair(Valor outro) {
        if (!this.currency.equals(outro.currency)) {
            throw new IllegalArgumentException("Não é possível subtrair moedas diferentes");
        }
        BigDecimal resultado = this.amount.subtract(outro.amount);
        if (resultado.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Resultado não pode ser negativo");
        }
        return new Valor(resultado, this.currency);
    }

    public Valor multiplicar(int quantidade) {
        if (quantidade < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }
        return new Valor(this.amount.multiply(new BigDecimal(quantidade)), this.currency);
    }
}

