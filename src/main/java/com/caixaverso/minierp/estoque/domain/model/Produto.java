package com.caixaverso.minierp.estoque.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Entidade: Produto (Aggregate Root do módulo Estoque)
 *
 * Representa um produto com controle de estoque disponível.
 */
@Entity
@Table(name = "produtos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private Integer quantidadeDisponivel;

    @Column(nullable = false)
    private Integer quantidadeReservada;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal preco;

    /**
     * Factory method
     */
    public static Produto criar(String nome, String descricao, Integer quantidade, BigDecimal preco) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório");
        }
        if (quantidade == null || quantidade < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }
        if (preco == null || preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }

        Produto produto = new Produto();
        produto.nome = nome;
        produto.descricao = descricao;
        produto.quantidadeDisponivel = quantidade;
        produto.quantidadeReservada = 0;
        produto.preco = preco;
        return produto;
    }

    /**
     * Verifica se há quantidade suficiente
     */
    public boolean temDisponibilidade(Integer quantidade) {
        return (this.quantidadeDisponivel - this.quantidadeReservada) >= quantidade;
    }

    /**
     * Reserva estoque
     */
    public void reservar(Integer quantidade) {
        if (!temDisponibilidade(quantidade)) {
            throw new IllegalStateException("Estoque insuficiente para " + nome);
        }
        this.quantidadeReservada += quantidade;
    }

    /**
     * Libera reserva (compensação)
     */
    public void liberarReserva(Integer quantidade) {
        if (this.quantidadeReservada < quantidade) {
            throw new IllegalStateException("Não há reserva suficiente para liberar");
        }
        this.quantidadeReservada -= quantidade;
    }

    /**
     * Confirma venda (transforma reserva em venda efetiva)
     */
    public void confirmarVenda(Integer quantidade) {
        if (this.quantidadeReservada < quantidade) {
            throw new IllegalStateException("Reserva insuficiente");
        }
        this.quantidadeReservada -= quantidade;
        this.quantidadeDisponivel -= quantidade;
    }
}
