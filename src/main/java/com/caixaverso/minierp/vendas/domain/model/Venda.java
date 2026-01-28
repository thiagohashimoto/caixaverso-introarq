package com.caixaverso.minierp.vendas.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Entidade: Venda (Aggregate Root)
 *
 * Conceito DDD: Raiz do agregado Venda.
 * - Tem identidade única (VendaId)
 * - Garante consistência dos dados internos
 * - Controla acesso aos itens da venda
 * - Publica eventos de domínio
 *
 * Conceito SOLID: Single Responsibility - apenas lógica de venda
 */
@Entity
@Table(name = "vendas")
@Data
@NoArgsConstructor
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long clienteId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusVenda status;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "venda_id")
    private List<ItemVenda> itens = new ArrayList<>();

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valorTotal;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime dataAtualizacao;

    /**
     * Construtor privado - usar factory method
     */
    private Venda(Long clienteId) {
        this.clienteId = clienteId;
        this.status = StatusVenda.PENDENTE;
        this.valorTotal = BigDecimal.ZERO;
    }

    /**
     * Factory method para criar nova venda
     */
    public static Venda criar(Long clienteId) {
        if (clienteId == null || clienteId <= 0) {
            throw new IllegalArgumentException("ClienteId inválido");
        }
        return new Venda(clienteId);
    }

    /**
     * Adiciona item à venda
     *
     * @throws IllegalStateException se venda não está PENDENTE
     */
    public void adicionarItem(Long produtoId, Integer quantidade, BigDecimal preco) {
        if (status != StatusVenda.PENDENTE) {
            throw new IllegalStateException(
                "Não é possível adicionar itens a uma venda " + status
            );
        }

        ItemVenda item = ItemVenda.criar(produtoId, quantidade, preco);
        this.itens.add(item);
        recalcularTotal();
    }

    /**
     * Remove item da venda
     */
    public void removerItem(Long produtoId) {
        if (status != StatusVenda.PENDENTE) {
            throw new IllegalStateException(
                "Não é possível remover itens de uma venda " + status
            );
        }

        boolean removido = itens.removeIf(item -> item.getProdutoId().equals(produtoId));
        if (removido) {
            recalcularTotal();
        }
    }

    /**
     * Confirma a venda
     */
    public void confirmar() {
        if (status != StatusVenda.PENDENTE) {
            throw new IllegalStateException("Venda já foi " + status);
        }

        if (itens.isEmpty()) {
            throw new IllegalStateException("Não é possível confirmar venda sem itens");
        }

        this.status = StatusVenda.CONFIRMADA;
    }

    /**
     * Cancela a venda
     */
    public void cancelar() {
        if (status == StatusVenda.ENTREGUE) {
            throw new IllegalStateException("Não é possível cancelar venda entregue");
        }

        this.status = StatusVenda.CANCELADA;
    }

    /**
     * Marca como entregue
     */
    public void entregar() {
        if (status != StatusVenda.CONFIRMADA) {
            throw new IllegalStateException("Apenas vendas confirmadas podem ser entregues");
        }

        this.status = StatusVenda.ENTREGUE;
    }

    /**
     * Retorna cópia imutável dos itens
     */
    public List<ItemVenda> obterItens() {
        return Collections.unmodifiableList(itens);
    }

    /**
     * Recalcula o valor total da venda
     */
    private void recalcularTotal() {
        this.valorTotal = itens.stream()
            .map(ItemVenda::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Verifica se venda está pronta para processamento
     */
    public boolean estaPendente() {
        return this.status == StatusVenda.PENDENTE;
    }

    public boolean estaConfirmada() {
        return this.status == StatusVenda.CONFIRMADA;
    }

    public boolean estaCancelada() {
        return this.status == StatusVenda.CANCELADA;
    }
}
