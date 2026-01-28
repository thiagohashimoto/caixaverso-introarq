package com.caixaverso.minierp.financeiro.application.mapper;

import com.caixaverso.minierp.financeiro.domain.model.Cobranca;
import com.caixaverso.minierp.financeiro.application.dto.CobrancaResponseDTO;
import org.springframework.stereotype.Component;

/**
 * Mapper: CobrancaMapper
 */
@Component
public class CobrancaMapper {

    public CobrancaResponseDTO toResponseDTO(Cobranca cobranca) {
        return new CobrancaResponseDTO(
            cobranca.getId(),
            cobranca.getVendaId(),
            cobranca.getClienteId(),
            cobranca.getStatus().name(),
            cobranca.getValor(),
            cobranca.getDataPagamento(),
            cobranca.getDataCriacao(),
            cobranca.getDataAtualizacao()
        );
    }
}

