package com.caixaverso.minierp.estoque.application.service;

import com.caixaverso.minierp.estoque.domain.model.Produto;
import com.caixaverso.minierp.estoque.domain.service.ReservarEstoqueService;
import com.caixaverso.minierp.estoque.domain.repository.ProdutoRepository;
import com.caixaverso.minierp.estoque.application.dto.ProdutoResponseDTO;
import com.caixaverso.minierp.estoque.application.dto.CriarProdutoDTO;
import com.caixaverso.minierp.estoque.application.mapper.ProdutoMapper;
import com.caixaverso.minierp.shared.domain.exception.EntidadeNaoEncontradaException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serviço de Aplicação: EstoqueApplicationService
 */
@Service
@AllArgsConstructor
@Transactional
public class EstoqueApplicationService {

    private final ReservarEstoqueService reservarEstoqueService;
    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper mapper;

    /**
     * Reserva estoque para uma venda
     */
    public void reservarEstoque(Long vendaId, Long produtoId, Integer quantidade) {
        reservarEstoqueService.executar(vendaId, produtoId, quantidade);
    }

    /**
     * Obtém produto por ID
     */
    @Transactional(readOnly = true)
    public ProdutoResponseDTO obter(Long produtoId) {
        Produto produto = produtoRepository.findById(produtoId)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Produto", produtoId));
        return mapper.toResponseDTO(produto);
    }

    /**
     * Lista todos os produtos
     */
    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listar() {
        return produtoRepository.findAll().stream()
            .map(mapper::toResponseDTO)
            .toList();
    }

    /**
     * Cria um novo produto
     */
    public ProdutoResponseDTO criarProduto(CriarProdutoDTO dto) {
        Produto produto = Produto.criar(dto.getNome(), dto.getDescricao(), dto.getQuantidadeDisponivel(), dto.getPreco());
        Produto produtoSalvo = produtoRepository.save(produto);
        return mapper.toResponseDTO(produtoSalvo);
    }

    /**
     * Verifica se há disponibilidade de estoque
     */
    @Transactional(readOnly = true)
    public boolean verificarDisponibilidade(Long produtoId, Integer quantidadeRequerida) {
        Produto produto = produtoRepository.findById(produtoId)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Produto", produtoId));
        return produto.temDisponibilidade(quantidadeRequerida);
    }
}
