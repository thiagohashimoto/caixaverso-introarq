package com.caixaverso.minierp.vendas.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DTO: CriarVendaDTO
 *
 * Conceito SOLID: Data Transfer Object separa dados externos
 * de conceitos de domínio. Controllers recebem DTOs, não entidades.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CriarVendaDTO {

    @NotNull(message = "ClienteId é obrigatório")
    @Positive(message = "ClienteId deve ser positivo")
    private Long clienteId;

    @NotNull(message = "ProdutoId é obrigatório")
    @Positive(message = "ProdutoId deve ser positivo")
    private Long produtoId;

    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser maior que zero")
    private Integer quantidade;

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    private BigDecimal preco;
}
