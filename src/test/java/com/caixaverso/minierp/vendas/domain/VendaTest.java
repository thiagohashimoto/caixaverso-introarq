package com.caixaverso.minierp.vendas.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes Unitários: VendaTest
 *
 * Testa a lógica de negócio pura do agregado Venda.
 * Sem Spring, sem banco de dados - apenas regras de domínio.
 */
@DisplayName("Testes de Domínio - Venda")
class VendaTest {

    @Test
    @DisplayName("Deve criar venda com status PENDENTE")
    void deveCriarVendaComStatusPendente() {
        // Arrange & Act
        Venda venda = Venda.criar(1L);

        // Assert
        assertEquals(StatusVenda.PENDENTE, venda.getStatus());
        assertTrue(venda.obterItens().isEmpty());
        assertEquals(BigDecimal.ZERO, venda.getValorTotal());
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar venda com clienteId inválido")
    void deveLancarExcecaoClienteInvalido() {
        assertThrows(IllegalArgumentException.class, () -> Venda.criar(null));
        assertThrows(IllegalArgumentException.class, () -> Venda.criar(-1L));
        assertThrows(IllegalArgumentException.class, () -> Venda.criar(0L));
    }

    @Test
    @DisplayName("Deve adicionar item à venda")
    void deveAdicionarItemAVenda() {
        // Arrange
        Venda venda = Venda.criar(1L);

        // Act
        venda.adicionarItem(1L, 5, new BigDecimal("100.00"));

        // Assert
        assertEquals(1, venda.obterItens().size());
        assertEquals(new BigDecimal("500.00"), venda.getValorTotal());
    }

    @Test
    @DisplayName("Deve lançar exceção ao adicionar item a venda não PENDENTE")
    void deveLancarExcecaoAoAdicionarItemAVendaNaoPendente() {
        // Arrange
        Venda venda = Venda.criar(1L);
        venda.adicionarItem(1L, 1, new BigDecimal("100.00"));
        venda.confirmar();

        // Act & Assert
        assertThrows(IllegalStateException.class,
            () -> venda.adicionarItem(2L, 1, new BigDecimal("100.00")));
    }

    @Test
    @DisplayName("Deve confirmar venda com itens")
    void deveConfirmarVendaComItens() {
        // Arrange
        Venda venda = Venda.criar(1L);
        venda.adicionarItem(1L, 2, new BigDecimal("50.00"));

        // Act
        venda.confirmar();

        // Assert
        assertEquals(StatusVenda.CONFIRMADA, venda.getStatus());
    }

    @Test
    @DisplayName("Deve lançar exceção ao confirmar venda sem itens")
    void deveLancarExcecaoAoConfirmarSemItens() {
        // Arrange
        Venda venda = Venda.criar(1L);

        // Act & Assert
        assertThrows(IllegalStateException.class, venda::confirmar);
    }

    @Test
    @DisplayName("Deve cancelar venda PENDENTE")
    void deveCancelarVendaPendente() {
        // Arrange
        Venda venda = Venda.criar(1L);
        venda.adicionarItem(1L, 1, new BigDecimal("100.00"));

        // Act
        venda.cancelar();

        // Assert
        assertEquals(StatusVenda.CANCELADA, venda.getStatus());
    }

    @Test
    @DisplayName("Deve lançar exceção ao cancelar venda ENTREGUE")
    void deveLancarExcecaoAoCancelarVendaEntregue() {
        // Arrange
        Venda venda = Venda.criar(1L);
        venda.adicionarItem(1L, 1, new BigDecimal("100.00"));
        venda.confirmar();
        venda.entregar();

        // Act & Assert
        assertThrows(IllegalStateException.class, venda::cancelar);
    }

    @Test
    @DisplayName("Deve remover item e recalcular total")
    void deveRemoverItemERecalcularTotal() {
        // Arrange
        Venda venda = Venda.criar(1L);
        venda.adicionarItem(1L, 2, new BigDecimal("50.00"));
        venda.adicionarItem(2L, 1, new BigDecimal("100.00"));
        assertEquals(new BigDecimal("200.00"), venda.getValorTotal());

        // Act
        venda.removerItem(1L);

        // Assert
        assertEquals(1, venda.obterItens().size());
        assertEquals(new BigDecimal("100.00"), venda.getValorTotal());
    }
}

