package com.caixaverso.minierp.financeiro.domain.model;

/**
 * Value Object: StatusCobranca (Enum)
 */
public enum StatusCobranca {
    CRIADA("Cobrança criada"),
    CONFIRMADA("Cobrança confirmada"),
    PAGA("Cobrança paga"),
    CANCELADA("Cobrança cancelada");

    private final String descricao;

    StatusCobranca(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

