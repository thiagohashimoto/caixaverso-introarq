package com.caixaverso.minierp.estoque.application;

import com.caixaverso.minierp.estoque.application.dto.ProdutoResponseDTO;
import com.caixaverso.minierp.estoque.application.dto.CriarProdutoDTO;
import com.caixaverso.minierp.estoque.application.service.EstoqueApplicationService;
import com.caixaverso.minierp.estoque.domain.model.Produto;
import com.caixaverso.minierp.estoque.domain.repository.ProdutoRepository;
import com.caixaverso.minierp.estoque.application.mapper.ProdutoMapper;
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
 * Testes de Aplicação: EstoqueApplicationServiceTest
 *
 * Testes do serviço de aplicação de Estoque.
 * Conceito SOLID: Testa a orquestração entre domain services e repositories.
 */
@DisplayName("Testes de Aplicação - EstoqueApplicationService")
@ExtendWith(MockitoExtension.class)
class EstoqueApplicationServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ProdutoMapper produtoMapper;

    @InjectMocks
    private EstoqueApplicationService estoqueApplicationService;

    private Produto produtoFixture;
    private ProdutoResponseDTO produtoDTOFixture;
    private CriarProdutoDTO criarProdutoDTOFixture;

    @BeforeEach
    void setUp() {
        // Fixture: Produto
        produtoFixture = Produto.criar("Notebook", "Notebook Dell", 50, new BigDecimal("2500.00"));
        produtoFixture.setId(1L);

        // Fixture: DTO Response
        produtoDTOFixture = new ProdutoResponseDTO(
            1L, "Notebook", "Notebook Dell", 50, 0, new BigDecimal("2500.00")
        );

        // Fixture: DTO Request
        criarProdutoDTOFixture = new CriarProdutoDTO(
            "Notebook", "Notebook Dell", 50, new BigDecimal("2500.00")
        );
    }

    @Test
    @DisplayName("Deve criar produto com sucesso")
    void deveCriarProdutoComSucesso() {
        // Arrange
        when(produtoRepository.save(any(Produto.class)))
            .thenReturn(produtoFixture);
        when(produtoMapper.toResponseDTO(produtoFixture))
            .thenReturn(produtoDTOFixture);

        // Act
        ProdutoResponseDTO result = estoqueApplicationService
            .criarProduto(criarProdutoDTOFixture);

        // Assert
        assertNotNull(result);
        assertEquals("Notebook", result.getNome());
        assertEquals(50, result.getQuantidadeDisponivel());

        verify(produtoRepository, times(1)).save(any(Produto.class));
        verify(produtoMapper, times(1)).toResponseDTO(any());
    }

    @Test
    @DisplayName("Deve obter produto por ID com sucesso")
    void deveObterProdutoPorIdComSucesso() {
        // Arrange
        Long produtoId = 1L;
        when(produtoRepository.findById(produtoId))
            .thenReturn(Optional.of(produtoFixture));
        when(produtoMapper.toResponseDTO(produtoFixture))
            .thenReturn(produtoDTOFixture);

        // Act
        ProdutoResponseDTO result = estoqueApplicationService.obter(produtoId);

        // Assert
        assertNotNull(result);
        assertEquals("Notebook", result.getNome());

        verify(produtoRepository, times(1)).findById(produtoId);
    }

    @Test
    @DisplayName("Deve lançar exceção ao obter produto inexistente")
    void deveLancarExcecaoAoObterProdutoInexistente() {
        // Arrange
        Long produtoId = 999L;
        when(produtoRepository.findById(produtoId))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(Exception.class, () -> estoqueApplicationService.obter(produtoId));

        verify(produtoRepository, times(1)).findById(produtoId);
    }

    @Test
    @DisplayName("Deve verificar disponibilidade com sucesso")
    void deveVerificarDisponibilidadeComSucesso() {
        // Arrange
        Long produtoId = 1L;
        Integer quantidadeRequerida = 10;

        when(produtoRepository.findById(produtoId))
            .thenReturn(Optional.of(produtoFixture));

        // Act
        boolean resultado = estoqueApplicationService
            .verificarDisponibilidade(produtoId, quantidadeRequerida);

        // Assert
        assertTrue(resultado);
        verify(produtoRepository, times(1)).findById(produtoId);
    }

    @Test
    @DisplayName("Deve retornar false quando não há disponibilidade")
    void deveRetornarFalseQuandoNaoHaDisponibilidade() {
        // Arrange
        Long produtoId = 1L;
        Integer quantidadeRequerida = 100; // Maior que os 50 disponíveis

        when(produtoRepository.findById(produtoId))
            .thenReturn(Optional.of(produtoFixture));

        // Act
        boolean resultado = estoqueApplicationService
            .verificarDisponibilidade(produtoId, quantidadeRequerida);

        // Assert
        assertFalse(resultado);
        verify(produtoRepository, times(1)).findById(produtoId);
    }
}

