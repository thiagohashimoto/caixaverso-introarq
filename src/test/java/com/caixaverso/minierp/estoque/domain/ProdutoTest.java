package com.caixaverso.minierp.estoque.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de Domínio: ProdutoTest
 *
 * Testa a lógica de negócio pura do agregado Produto.
 */
@DisplayName("Testes de Domínio - Produto")
class ProdutoTest {

    @Test
    @DisplayName("Deve criar produto com sucesso")
    void deveCriarProdutoComSucesso() {
        // Act
        Produto produto = Produto.criar("Notebook", "Dell XPS", 10, new BigDecimal("3000.00"));

        // Assert
        assertNotNull(produto);
        assertEquals("Notebook", produto.getNome());
        assertEquals(10, produto.getQuantidadeDisponivel());
        assertEquals(0, produto.getQuantidadeReservada());
    }

    @Test
    @DisplayName("Deve reservar estoque")
    void deveReservarEstoque() {
        // Arrange
        Produto produto = Produto.criar("Mouse", "Logitech", 20, new BigDecimal("50.00"));

        // Act
        produto.reservar(5);

        // Assert
        assertEquals(5, produto.getQuantidadeReservada());
        assertTrue(produto.temDisponibilidade(15));
    }

    @Test
    @DisplayName("Deve lançar exceção ao reservar estoque insuficiente")
    void deveLancarExcecaoAoReservarInsuficiente() {
        // Arrange
        Produto produto = Produto.criar("Teclado", "Mecânico", 5, new BigDecimal("200.00"));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> produto.reservar(10));
    }

    @Test
    @DisplayName("Deve liberar reserva")
    void deveLiberarReserva() {
        // Arrange
        Produto produto = Produto.criar("Monitor", "Samsung", 3, new BigDecimal("800.00"));
        produto.reservar(2);

        // Act
        produto.liberarReserva(1);

        // Assert
        assertEquals(1, produto.getQuantidadeReservada());
    }

    @Test
    @DisplayName("Deve confirmar venda")
    void deveConfirmarVenda() {
        // Arrange
        Produto produto = Produto.criar("Webcam", "HD", 15, new BigDecimal("100.00"));
        produto.reservar(8);

        // Act
        produto.confirmarVenda(8);

        // Assert
        assertEquals(0, produto.getQuantidadeReservada());
        assertEquals(7, produto.getQuantidadeDisponivel());
    }
}

