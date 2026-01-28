package com.caixaverso.minierp.estoque.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DTO: CriarProdutoDTO
 *
 * Conceito SOLID: Data Transfer Object separa dados externos
 * de conceitos de domínio. Controllers recebem DTOs, não entidades.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CriarProdutoDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 255, message = "Nome deve ter entre 3 e 255 caracteres")
    private String nome;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(min = 10, max = 1000, message = "Descrição deve ter entre 10 e 1000 caracteres")
    private String descricao;

    @NotNull(message = "Quantidade disponível é obrigatória")
    @Min(value = 1, message = "Quantidade disponível deve ser no mínimo 1")
    private Integer quantidadeDisponivel;

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    @Digits(integer = 10, fraction = 2, message = "Preço deve ter no máximo 10 dígitos inteiros e 2 decimais")
    private BigDecimal preco;
}
