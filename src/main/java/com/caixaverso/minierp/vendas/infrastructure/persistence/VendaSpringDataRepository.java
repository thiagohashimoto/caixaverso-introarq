package com.caixaverso.minierp.vendas.infrastructure.persistence;

import com.caixaverso.minierp.vendas.domain.model.Venda;
import com.caixaverso.minierp.vendas.domain.model.StatusVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface Spring Data JPA para Venda
 *
 * Herda operações CRUD básicas de JpaRepository.
 */
@Repository
public interface VendaSpringDataRepository extends JpaRepository<Venda, Long> {
    List<Venda> findByClienteId(Long clienteId);
    List<Venda> findByStatus(StatusVenda status);
}

