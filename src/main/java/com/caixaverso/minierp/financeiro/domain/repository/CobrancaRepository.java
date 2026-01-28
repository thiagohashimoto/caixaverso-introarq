package com.caixaverso.minierp.financeiro.domain.repository;

import com.caixaverso.minierp.financeiro.domain.model.Cobranca;
import java.util.List;
import java.util.Optional;

/**
 * Interface: CobrancaRepository
 */
public interface CobrancaRepository {

    Cobranca save(Cobranca cobranca);

    Optional<Cobranca> findById(Long id);

    Optional<Cobranca> findByVendaId(Long vendaId);

    List<Cobranca> findByClienteId(Long clienteId);

    List<Cobranca> findAll();

    void delete(Cobranca cobranca);
}
