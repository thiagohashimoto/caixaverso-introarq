package com.caixaverso.minierp.vendas.application.service;

import com.caixaverso.minierp.vendas.domain.model.Venda;
import com.caixaverso.minierp.vendas.domain.repository.VendaRepository;
import com.caixaverso.minierp.vendas.domain.service.CriarVendaService;
import com.caixaverso.minierp.vendas.application.dto.CriarVendaDTO;
import com.caixaverso.minierp.vendas.application.dto.VendaResponseDTO;
import com.caixaverso.minierp.vendas.application.mapper.VendaMapper;
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
 * Testes de Aplicação: VendaApplicationServiceTest
 *
 * Testa os casos de uso da aplicação.
 * Usa mocks para isolar a lógica de aplicação de dependências externas.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de Aplicação - VendaApplicationService")
class VendaApplicationServiceTest {

    @Mock
    private CriarVendaService criarVendaService;

    @Mock
    private VendaRepository vendaRepository;

    @Mock
    private VendaMapper mapper;

    @InjectMocks
    private VendaApplicationService service;

    private CriarVendaDTO dto;
    private Venda venda;
    private VendaResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        dto = new CriarVendaDTO(1L, 1L, 5, new BigDecimal("100.00"));
        venda = Venda.criar(1L);
        venda.adicionarItem(1L, 5, new BigDecimal("100.00"));
        responseDTO = new VendaResponseDTO();
        responseDTO.setId(1L);
    }

    @Test
    @DisplayName("Deve criar venda com sucesso")
    void deveCriarVendaComSucesso() {
        // Arrange
        when(criarVendaService.executar(anyLong(), anyLong(), anyInt(), any(BigDecimal.class)))
            .thenReturn(venda);
        when(mapper.toResponseDTO(venda))
            .thenReturn(responseDTO);

        // Act
        VendaResponseDTO resultado = service.criar(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(criarVendaService, times(1)).executar(anyLong(), anyLong(), anyInt(), any(BigDecimal.class));
    }

    @Test
    @DisplayName("Deve obter venda por ID")
    void deveObterVendaPorId() {
        // Arrange
        when(vendaRepository.findById(1L))
            .thenReturn(Optional.of(venda));
        when(mapper.toResponseDTO(venda))
            .thenReturn(responseDTO);

        // Act
        VendaResponseDTO resultado = service.obter(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção ao obter venda inexistente")
    void deveLancarExcecaoAoObterVendaInexistente() {
        // Arrange
        when(vendaRepository.findById(999L))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(Exception.class, () -> service.obter(999L));
    }
}

