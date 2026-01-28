package com.caixaverso.minierp.estoque.application.mapper;

import com.caixaverso.minierp.estoque.domain.model.Produto;
import com.caixaverso.minierp.estoque.application.dto.ProdutoResponseDTO;
import org.springframework.stereotype.Component;

/**
 * Mapper: ProdutoMapper
 */
@Component
public class ProdutoMapper {

    public ProdutoResponseDTO toResponseDTO(Produto produto) {
        return new ProdutoResponseDTO(
            produto.getId(),
            produto.getNome(),
            produto.getDescricao(),
            produto.getQuantidadeDisponivel(),
            produto.getQuantidadeReservada(),
            produto.getPreco()
        );
    }
}

