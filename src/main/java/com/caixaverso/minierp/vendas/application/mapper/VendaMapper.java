package com.caixaverso.minierp.vendas.application.mapper;

import com.caixaverso.minierp.vendas.domain.model.Venda;
import com.caixaverso.minierp.vendas.domain.model.ItemVenda;
import com.caixaverso.minierp.vendas.application.dto.CriarVendaDTO;
import com.caixaverso.minierp.vendas.application.dto.VendaResponseDTO;
import org.springframework.stereotype.Component;

/**
 * Mapper: VendaMapper
 *
 * Conceito SOLID: Conversão entre camadas isolada em uma classe.
 * Evita acoplamento entre DTOs e entidades de domínio.
 */
@Component
public class VendaMapper {

    /**
     * Converte DTO de entrada para entidade de domínio
     */
    public Venda toDomain(CriarVendaDTO dto) {
        return Venda.criar(dto.getClienteId());
    }

    /**
     * Converte entidade de domínio para DTO de resposta
     */
    public VendaResponseDTO toResponseDTO(Venda venda) {
        VendaResponseDTO dto = new VendaResponseDTO();
        dto.setId(venda.getId());
        dto.setClienteId(venda.getClienteId());
        dto.setStatus(venda.getStatus().name());
        dto.setValorTotal(venda.getValorTotal());
        dto.setDataCriacao(venda.getDataCriacao());
        dto.setDataAtualizacao(venda.getDataAtualizacao());

        // Mapear itens
        dto.setItens(venda.obterItens().stream()
            .map(this::toItemDTO)
            .toList());

        return dto;
    }

    private VendaResponseDTO.ItemVendaDTO toItemDTO(ItemVenda item) {
        VendaResponseDTO.ItemVendaDTO dto = new VendaResponseDTO.ItemVendaDTO();
        dto.setId(item.getId());
        dto.setProdutoId(item.getProdutoId());
        dto.setQuantidade(item.getQuantidade());
        dto.setPreco(item.getPreco());
        dto.setSubtotal(item.getSubtotal());
        return dto;
    }
}

