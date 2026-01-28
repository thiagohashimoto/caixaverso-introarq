package com.caixaverso.minierp.vendas.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Entidade: ItemVenda
 *
 * Conceito DDD: Entidade interna de um agregado.
 * Tem identidade dentro do contexto de Venda, mas não pode existir sozinha.
 * Acesso controlado apenas pela raiz do agregado (Venda).
 */
@Entity
@Table(name = "itens_venda")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long produtoId;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal preco;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal subtotal;

    /**
     * Construtor package-private para criar item.
     * Apenas Venda pode instanciar ItemVenda.
     */
    ItemVenda(Long produtoId, Integer quantidade, BigDecimal preco) {
        validar(produtoId, quantidade, preco);
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.preco = preco;
        this.subtotal = preco.multiply(new BigDecimal(quantidade));
    }

    /**
     * Factory method package-private
     */
    static ItemVenda criar(Long produtoId, Integer quantidade, BigDecimal preco) {
        return new ItemVenda(produtoId, quantidade, preco);
    }

    private static void validar(Long produtoId, Integer quantidade, BigDecimal preco) {
        if (produtoId == null || produtoId <= 0) {
            throw new IllegalArgumentException("ProdutoId inválido");
        }
        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        if (preco == null || preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }
    }
}
