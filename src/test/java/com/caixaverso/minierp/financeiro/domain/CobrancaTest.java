package com.caixaverso.minierp.financeiro.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de Domínio: CobrancaTest
 *
 * Testa a lógica de negócio pura do agregado Cobranca.
 */
@DisplayName("Testes de Domínio - Cobranca")
class CobrancaTest {

    @Test
    @DisplayName("Deve criar cobrança com sucesso")
    void deveCriarCobrancaComSucesso() {
        // Act
        Cobranca cobranca = Cobranca.criar(1L, 10L, new BigDecimal("500.00"));

        // Assert
        assertNotNull(cobranca);
        assertEquals(1L, cobranca.getVendaId());
        assertEquals(10L, cobranca.getClienteId());
        assertEquals(StatusCobranca.CRIADA, cobranca.getStatus());
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar cobrança com valor inválido")
    void deveLancarExcecaoValorInvalido() {
        assertThrows(IllegalArgumentException.class,
            () -> Cobranca.criar(1L, 10L, BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class,
            () -> Cobranca.criar(1L, 10L, new BigDecimal("-100.00")));
    }

    @Test
    @DisplayName("Deve confirmar cobrança")
    void deveConfirmarCobranca() {
        // Arrange
        Cobranca cobranca = Cobranca.criar(1L, 10L, new BigDecimal("500.00"));

        // Act
        cobranca.confirmar();

        // Assert
        assertEquals(StatusCobranca.CONFIRMADA, cobranca.getStatus());
    }

    @Test
    @DisplayName("Deve registrar pagamento")
    void deveRegistrarPagamento() {
        // Arrange
        Cobranca cobranca = Cobranca.criar(1L, 10L, new BigDecimal("500.00"));
        cobranca.confirmar();

        // Act
        cobranca.pagar();

        // Assert
        assertEquals(StatusCobranca.PAGA, cobranca.getStatus());
        assertNotNull(cobranca.getDataPagamento());
    }

    @Test
    @DisplayName("Deve cancelar cobrança")
    void deveCancelarCobranca() {
        // Arrange
        Cobranca cobranca = Cobranca.criar(1L, 10L, new BigDecimal("500.00"));

        // Act
        cobranca.cancelar();

        // Assert
        assertEquals(StatusCobranca.CANCELADA, cobranca.getStatus());
    }

    @Test
    @DisplayName("Deve lançar exceção ao cancelar cobrança já paga")
    void deveLancarExcecaoAoCancelarPaga() {
        // Arrange
        Cobranca cobranca = Cobranca.criar(1L, 10L, new BigDecimal("500.00"));
        cobranca.confirmar();
        cobranca.pagar();

        // Act & Assert
        assertThrows(IllegalStateException.class, cobranca::cancelar);
    }
}

