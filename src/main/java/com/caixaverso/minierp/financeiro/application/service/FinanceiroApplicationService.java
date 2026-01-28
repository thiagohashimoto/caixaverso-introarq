package com.caixaverso.minierp.financeiro.application.service;

import com.caixaverso.minierp.financeiro.domain.model.Cobranca;
import com.caixaverso.minierp.financeiro.domain.service.CriarCobrancaService;
import com.caixaverso.minierp.financeiro.domain.repository.CobrancaRepository;
import com.caixaverso.minierp.financeiro.application.dto.CobrancaResponseDTO;
import com.caixaverso.minierp.financeiro.application.mapper.CobrancaMapper;
import com.caixaverso.minierp.shared.domain.exception.EntidadeNaoEncontradaException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Serviço de Aplicação: FinanceiroApplicationService
 */
@Service
@AllArgsConstructor
@Transactional
public class FinanceiroApplicationService {

    private final CriarCobrancaService criarCobrancaService;
    private final CobrancaRepository cobrancaRepository;
    private final CobrancaMapper mapper;

    /**
     * Cria cobrança para venda
     */
    public CobrancaResponseDTO criarCobranca(Long vendaId, Long clienteId, BigDecimal valor) {
        criarCobrancaService.executar(vendaId, clienteId, valor);

        Cobranca cobranca = cobrancaRepository.findByVendaId(vendaId)
            .orElseThrow(() -> new IllegalStateException("Cobrança não encontrada"));

        return mapper.toResponseDTO(cobranca);
    }

    /**
     * Obtém cobrança por ID
     */
    @Transactional(readOnly = true)
    public CobrancaResponseDTO obter(Long cobrancaId) {
        Cobranca cobranca = cobrancaRepository.findById(cobrancaId)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Cobrança", cobrancaId));

        return mapper.toResponseDTO(cobranca);
    }

    /**
     * Lista cobranças de um cliente
     */
    @Transactional(readOnly = true)
    public List<CobrancaResponseDTO> listarPorCliente(Long clienteId) {
        return cobrancaRepository.findByClienteId(clienteId).stream()
            .map(mapper::toResponseDTO)
            .toList();
    }

    /**
     * Registra pagamento de cobrança
     */
    public CobrancaResponseDTO pagar(Long cobrancaId) {
        Cobranca cobranca = cobrancaRepository.findById(cobrancaId)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Cobrança", cobrancaId));

        cobranca.pagar();
        cobrancaRepository.save(cobranca);

        return mapper.toResponseDTO(cobranca);
    }

    /**
     * Lista todas as cobranças
     */
    @Transactional(readOnly = true)
    public List<CobrancaResponseDTO> listar() {
        return cobrancaRepository.findAll().stream()
            .map(mapper::toResponseDTO)
            .toList();
    }
}

