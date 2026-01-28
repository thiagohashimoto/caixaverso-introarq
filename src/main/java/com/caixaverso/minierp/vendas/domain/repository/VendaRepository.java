package com.caixaverso.minierp.vendas.domain.repository;

import com.caixaverso.minierp.vendas.domain.model.Venda;
import com.caixaverso.minierp.vendas.domain.model.StatusVenda;
import java.util.List;
import java.util.Optional;

/**
 * Interface: VendaRepository
 *
 * Conceito DDD: Repository abstrai a persistência.
 * Domain não conhece detalhes de como dados são salvos.
 *
 * Conceito SOLID: Dependency Inversion - domain depende de abstrações,
 * não de implementações concretas de persistência.
 */
public interface VendaRepository {

    /**
     * Salva uma venda
     */
    void save(Venda venda);

    /**
     * Busca venda por ID
     */
    Optional<Venda> findById(Long id);

    /**
     * Lista todas as vendas de um cliente
     */
    List<Venda> findByClienteId(Long clienteId);

    /**
     * Lista vendas por status
     */
    List<Venda> findByStatus(StatusVenda status);

    /**
     * Lista todas as vendas
     */
    List<Venda> findAll();

    /**
     * Deleta uma venda
     */
    void delete(Venda venda);
}

