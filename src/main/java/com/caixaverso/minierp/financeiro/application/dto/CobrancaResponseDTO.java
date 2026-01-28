package com.caixaverso.minierp.financeiro.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO: CobrancaResponseDTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CobrancaResponseDTO {
    private Long id;
    private Long vendaId;
    private Long clienteId;
    private String status;
    private BigDecimal valor;
    private LocalDateTime dataPagamento;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
}

