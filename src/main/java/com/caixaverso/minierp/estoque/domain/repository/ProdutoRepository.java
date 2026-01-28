package com.caixaverso.minierp.estoque.domain.repository;

import com.caixaverso.minierp.estoque.domain.model.Produto;
import java.util.List;
import java.util.Optional;

/**
 * Interface: ProdutoRepository
 *
 * Abstração de persistência para Produto.
 */
public interface ProdutoRepository {

    Produto save(Produto produto);

    Optional<Produto> findById(Long id);

    List<Produto> findAll();

    void delete(Produto produto);
}
