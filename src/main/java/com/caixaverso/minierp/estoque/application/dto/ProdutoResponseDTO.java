package com.caixaverso.minierp.estoque.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO: ProdutoResponseDTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoResponseDTO {
    private Long id;
    private String nome;
    private String descricao;
    private Integer quantidadeDisponivel;
    private Integer quantidadeReservada;
    private BigDecimal preco;

    public Integer getQuantidadeAtual() {
        return quantidadeDisponivel - quantidadeReservada;
    }
}

