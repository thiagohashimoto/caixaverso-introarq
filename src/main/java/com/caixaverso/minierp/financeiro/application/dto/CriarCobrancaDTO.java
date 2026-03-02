package com.caixaverso.minierp.financeiro.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * DTO: CriarCobrancaDTO
 *
 * Request DTO para criação de cobrança via API.
 * Valida os dados de entrada antes de chegar ao serviço de domínio.
 *
 * Campos obrigatórios:
 * - vendaId: ID da venda associada
 * - clienteId: ID do cliente
 * - valor: Valor da cobrança
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CriarCobrancaDTO {

    /**
     * ID da venda associada à cobrança
     */
    @NotNull(message = "vendaId é obrigatório")
    @Positive(message = "vendaId deve ser maior que zero")
    private Long vendaId;

    /**
     * ID do cliente que será cobrado
     */
    @NotNull(message = "clienteId é obrigatório")
    @Positive(message = "clienteId deve ser maior que zero")
    private Long clienteId;

    /**
     * Valor da cobrança
     */
    @NotNull(message = "valor é obrigatório")
    @DecimalMin(value = "0.01", message = "valor deve ser maior que zero")
    private BigDecimal valor;
}
