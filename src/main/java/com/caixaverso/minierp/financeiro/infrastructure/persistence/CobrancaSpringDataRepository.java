package com.caixaverso.minierp.financeiro.infrastructure.persistence;

import com.caixaverso.minierp.financeiro.domain.model.Cobranca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data Repository para Cobranca
 */
@Repository
public interface CobrancaSpringDataRepository extends JpaRepository<Cobranca, Long> {
    Optional<Cobranca> findByVendaId(Long vendaId);
    List<Cobranca> findByClienteId(Long clienteId);
}

