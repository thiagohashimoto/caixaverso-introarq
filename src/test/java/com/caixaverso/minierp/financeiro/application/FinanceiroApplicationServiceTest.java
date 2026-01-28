package com.caixaverso.minierp.financeiro.application.service;

import com.caixaverso.minierp.financeiro.application.dto.CobrancaResponseDTO;
import com.caixaverso.minierp.financeiro.domain.model.Cobranca;
import com.caixaverso.minierp.financeiro.domain.repository.CobrancaRepository;
import com.caixaverso.minierp.financeiro.domain.service.CriarCobrancaService;
import com.caixaverso.minierp.financeiro.application.mapper.CobrancaMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes de Aplicação: FinanceiroApplicationServiceTest
 *
 * Testes do serviço de aplicação de Financeiro.
 * Conceito SOLID: Testa a orquestração entre domain services e repositories.
 */
@DisplayName("Testes de Aplicação - FinanceiroApplicationService")
@ExtendWith(MockitoExtension.class)
class FinanceiroApplicationServiceTest {

    @Mock
    private CriarCobrancaService criarCobrancaService;

    @Mock
    private CobrancaRepository cobrancaRepository;

    @Mock
    private CobrancaMapper cobrancaMapper;

    @InjectMocks
    private FinanceiroApplicationService financeiroApplicationService;

    private Cobranca cobrancaFixture;
    private CobrancaResponseDTO cobrancaDTOFixture;

    @BeforeEach
    void setUp() {
        // Fixture: Cobrança
        cobrancaFixture = Cobranca.criar(1L, 10L, new BigDecimal("500.00"));
        cobrancaFixture.setId(100L);

        // Fixture: DTO
        cobrancaDTOFixture = new CobrancaResponseDTO(
            100L, 1L, 10L, "CRIADA", new BigDecimal("500.00"), null, null, null
        );
    }

    @Test
    @DisplayName("Deve criar cobrança com sucesso")
    void deveCriarCobrancaComSucesso() {
        // Arrange
        Long vendaId = 1L;
        Long clienteId = 10L;
        BigDecimal valor = new BigDecimal("500.00");

        // Mock do criarCobrancaService para salvar a cobrança
        doAnswer(invocation -> {
            cobrancaRepository.save(cobrancaFixture);
            return null;
        }).when(criarCobrancaService).executar(vendaId, clienteId, valor);

        when(cobrancaRepository.findByVendaId(vendaId))
            .thenReturn(Optional.of(cobrancaFixture));
        when(cobrancaMapper.toResponseDTO(cobrancaFixture))
            .thenReturn(cobrancaDTOFixture);

        // Act
        CobrancaResponseDTO result = financeiroApplicationService
            .criarCobranca(vendaId, clienteId, valor);

        // Assert
        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals(vendaId, result.getVendaId());
        assertEquals(clienteId, result.getClienteId());
        assertEquals("CRIADA", result.getStatus());

        verify(criarCobrancaService, times(1)).executar(vendaId, clienteId, valor);
        verify(cobrancaRepository, times(1)).findByVendaId(vendaId);
        verify(cobrancaMapper, times(1)).toResponseDTO(any());
    }

    @Test
    @DisplayName("Deve obter cobrança por ID com sucesso")
    void deveObterCobrancaPorIdComSucesso() {
        // Arrange
        Long cobrancaId = 100L;
        when(cobrancaRepository.findById(cobrancaId))
            .thenReturn(Optional.of(cobrancaFixture));
        when(cobrancaMapper.toResponseDTO(cobrancaFixture))
            .thenReturn(cobrancaDTOFixture);

        // Act
        CobrancaResponseDTO result = financeiroApplicationService.obter(cobrancaId);

        // Assert
        assertNotNull(result);
        assertEquals(100L, result.getId());

        verify(cobrancaRepository, times(1)).findById(cobrancaId);
    }

    @Test
    @DisplayName("Deve lançar exceção ao obter cobrança inexistente")
    void deveLancarExcecaoAoObterCobrancaInexistente() {
        // Arrange
        Long cobrancaId = 999L;
        when(cobrancaRepository.findById(cobrancaId))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(Exception.class, () -> financeiroApplicationService.obter(cobrancaId));

        verify(cobrancaRepository, times(1)).findById(cobrancaId);
    }

    @Test
    @DisplayName("Deve registrar pagamento com sucesso")
    void deveRegistrarPagamentoComSucesso() {
        // Arrange
        Long cobrancaId = 100L;
        cobrancaFixture.confirmar(); // Deve estar confirmada para pagar

        when(cobrancaRepository.findById(cobrancaId))
            .thenReturn(Optional.of(cobrancaFixture));
        when(cobrancaRepository.save(any(Cobranca.class)))
            .thenReturn(cobrancaFixture);
        when(cobrancaMapper.toResponseDTO(cobrancaFixture))
            .thenReturn(cobrancaDTOFixture);

        // Act
        CobrancaResponseDTO result = financeiroApplicationService.pagar(cobrancaId);

        // Assert
        assertNotNull(result);
        verify(cobrancaRepository, times(1)).findById(cobrancaId);
        verify(cobrancaRepository, times(1)).save(any(Cobranca.class));
    }
}
