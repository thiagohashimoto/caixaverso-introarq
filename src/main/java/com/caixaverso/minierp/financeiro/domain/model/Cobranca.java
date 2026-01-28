package com.caixaverso.minierp.financeiro.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade: Cobranca (Aggregate Root do módulo Financeiro)
 *
 * Representa uma cobrança gerada a partir de uma venda.
 */
@Entity
@Table(name = "cobrancas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cobranca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long vendaId;

    @Column(nullable = false)
    private Long clienteId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusCobranca status;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valor;

    @Column(nullable = true)
    private LocalDateTime dataPagamento;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime dataAtualizacao;

    /**
     * Factory method
     */
    public static Cobranca criar(Long vendaId, Long clienteId, BigDecimal valor) {
        if (vendaId == null || vendaId <= 0) {
            throw new IllegalArgumentException("VendaId inválido");
        }
        if (clienteId == null || clienteId <= 0) {
            throw new IllegalArgumentException("ClienteId inválido");
        }
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero");
        }

        Cobranca cobranca = new Cobranca();
        cobranca.vendaId = vendaId;
        cobranca.clienteId = clienteId;
        cobranca.status = StatusCobranca.CRIADA;
        cobranca.valor = valor;
        return cobranca;
    }

    /**
     * Confirma a cobrança
     */
    public void confirmar() {
        if (status != StatusCobranca.CRIADA) {
            throw new IllegalStateException("Cobrança já foi " + status);
        }
        this.status = StatusCobranca.CONFIRMADA;
    }

    /**
     * Registra pagamento
     */
    public void pagar() {
        if (status != StatusCobranca.CONFIRMADA) {
            throw new IllegalStateException("Apenas cobranças confirmadas podem ser pagas");
        }
        this.status = StatusCobranca.PAGA;
        this.dataPagamento = LocalDateTime.now();
    }

    /**
     * Cancela a cobrança
     */
    public void cancelar() {
        if (status == StatusCobranca.PAGA) {
            throw new IllegalStateException("Não é possível cancelar cobrança já paga");
        }
        this.status = StatusCobranca.CANCELADA;
    }
}
