package com.caixaverso.minierp.vendas.infrastructure.persistence;

import com.caixaverso.minierp.vendas.domain.model.Venda;
import com.caixaverso.minierp.vendas.domain.model.StatusVenda;
import com.caixaverso.minierp.vendas.domain.repository.VendaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementação: VendaJpaRepository
 *
 * Conceito SOLID: Dependency Inversion
 * Implementa a interface VendaRepository definida no domain.
 * Domain não conhece que está usando JPA.
 */
@Repository
@AllArgsConstructor
public class VendaJpaRepository implements VendaRepository {

    private final VendaSpringDataRepository springDataRepository;

    @Override
    public void save(Venda venda) {
        springDataRepository.save(venda);
    }

    @Override
    public Optional<Venda> findById(Long id) {
        return springDataRepository.findById(id);
    }

    @Override
    public List<Venda> findByClienteId(Long clienteId) {
        return springDataRepository.findByClienteId(clienteId);
    }

    @Override
    public List<Venda> findByStatus(StatusVenda status) {
        return springDataRepository.findByStatus(status);
    }

    @Override
    public List<Venda> findAll() {
        return springDataRepository.findAll();
    }

    @Override
    public void delete(Venda venda) {
        springDataRepository.delete(venda);
    }
}

