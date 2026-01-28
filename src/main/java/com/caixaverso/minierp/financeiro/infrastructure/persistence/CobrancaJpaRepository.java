package com.caixaverso.minierp.financeiro.infrastructure.persistence;

import com.caixaverso.minierp.financeiro.domain.model.Cobranca;
import com.caixaverso.minierp.financeiro.domain.repository.CobrancaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementação: CobrancaJpaRepository
 */
@Repository
@AllArgsConstructor
public class CobrancaJpaRepository implements CobrancaRepository {

    private final CobrancaSpringDataRepository springDataRepository;

    @Override
    public Cobranca save(Cobranca cobranca) {
        return springDataRepository.save(cobranca);
    }

    @Override
    public Optional<Cobranca> findById(Long id) {
        return springDataRepository.findById(id);
    }

    @Override
    public Optional<Cobranca> findByVendaId(Long vendaId) {
        return springDataRepository.findByVendaId(vendaId);
    }

    @Override
    public List<Cobranca> findByClienteId(Long clienteId) {
        return springDataRepository.findByClienteId(clienteId);
    }

    @Override
    public List<Cobranca> findAll() {
        return springDataRepository.findAll();
    }

    @Override
    public void delete(Cobranca cobranca) {
        springDataRepository.delete(cobranca);
    }
}
