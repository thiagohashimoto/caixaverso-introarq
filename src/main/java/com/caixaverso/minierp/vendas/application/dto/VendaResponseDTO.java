package com.caixaverso.minierp.vendas.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO: VendaResponseDTO
 *
 * Retorno padrão para operações de venda.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendaResponseDTO {

    private Long id;
    private Long clienteId;
    private String status;
    private BigDecimal valorTotal;
    private List<ItemVendaDTO> itens;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemVendaDTO {
        private Long id;
        private Long produtoId;
        private Integer quantidade;
        private BigDecimal preco;
        private BigDecimal subtotal;
    }
}

