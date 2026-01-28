package com.caixaverso.minierp.vendas.domain.model;

/**
 * Value Object: StatusVenda (Enum)
 *
 * Encapsula os estados possíveis de uma venda.
 */
public enum StatusVenda {
    PENDENTE("Pendente de confirmação"),
    CONFIRMADA("Confirmada e em processamento"),
    CANCELADA("Cancelada"),
    ENTREGUE("Entregue ao cliente");

    private final String descricao;

    StatusVenda(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
