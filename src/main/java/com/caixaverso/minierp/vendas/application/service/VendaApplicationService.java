package com.caixaverso.minierp.vendas.application.service;

import com.caixaverso.minierp.vendas.domain.model.Venda;
import com.caixaverso.minierp.vendas.domain.service.CriarVendaService;
import com.caixaverso.minierp.vendas.domain.service.ConfirmarVendaService;
import com.caixaverso.minierp.vendas.domain.repository.VendaRepository;
import com.caixaverso.minierp.vendas.application.dto.CriarVendaDTO;
import com.caixaverso.minierp.vendas.application.dto.VendaResponseDTO;
import com.caixaverso.minierp.vendas.application.mapper.VendaMapper;
import com.caixaverso.minierp.shared.domain.exception.EntidadeNaoEncontradaException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serviço de Aplicação: VendaApplicationService
 *
 * Conceito DDD: Application Services orquestram Domain Services
 * e convertem entre DTOs (API) e entidades de domínio.
 *
 * Conceito SOLID: Dependency Inversion - depende de serviços de domínio.
 */
@Service
@AllArgsConstructor
@Transactional
public class VendaApplicationService {

    private final CriarVendaService criarVendaService;
    private final ConfirmarVendaService confirmarVendaService;
    private final VendaRepository vendaRepository;
    private final VendaMapper mapper;

    /**
     * Cria uma nova venda
     */
    public VendaResponseDTO criar(CriarVendaDTO dto) {
        // Converter DTO para parâmetros de domínio
        Venda venda = criarVendaService.executar(
            dto.getClienteId(),
            dto.getProdutoId(),
            dto.getQuantidade(),
            dto.getPreco()
        );

        // Converter resultado para DTO
        return mapper.toResponseDTO(venda);
    }

    /**
     * Confirma uma venda (após sucesso de estoque e cobrança - SAGA)
     */
    public VendaResponseDTO confirmar(Long vendaId) {
        confirmarVendaService.executar(vendaId);

        Venda venda = vendaRepository.findById(vendaId)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Venda", vendaId));

        return mapper.toResponseDTO(venda);
    }

    /**
     * Obtém uma venda por ID
     */
    @Transactional(readOnly = true)
    public VendaResponseDTO obter(Long vendaId) {
        Venda venda = vendaRepository.findById(vendaId)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Venda", vendaId));

        return mapper.toResponseDTO(venda);
    }

    /**
     * Lista todas as vendas de um cliente
     */
    @Transactional(readOnly = true)
    public List<VendaResponseDTO> listarPorCliente(Long clienteId) {
        return vendaRepository.findByClienteId(clienteId).stream()
            .map(mapper::toResponseDTO)
            .toList();
    }

    /**
     * Lista todas as vendas
     */
    @Transactional(readOnly = true)
    public List<VendaResponseDTO> listar() {
        return vendaRepository.findAll().stream()
            .map(mapper::toResponseDTO)
            .toList();
    }

    /**
     * Cancela uma venda
     */
    public void cancelar(Long vendaId) {
        Venda venda = vendaRepository.findById(vendaId)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Venda", vendaId));

        venda.cancelar();
        vendaRepository.save(venda);
    }
}

