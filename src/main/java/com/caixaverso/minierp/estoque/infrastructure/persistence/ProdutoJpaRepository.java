package com.caixaverso.minierp.estoque.infrastructure.persistence;

import com.caixaverso.minierp.estoque.domain.model.Produto;
import com.caixaverso.minierp.estoque.domain.repository.ProdutoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementação: ProdutoJpaRepository
 */
@Repository
@AllArgsConstructor
public class ProdutoJpaRepository implements ProdutoRepository {

    private final ProdutoSpringDataRepository springDataRepository;

    @Override
    public Produto save(Produto produto) {
        return springDataRepository.save(produto);
    }

    @Override
    public Optional<Produto> findById(Long id) {
        return springDataRepository.findById(id);
    }

    @Override
    public List<Produto> findAll() {
        return springDataRepository.findAll();
    }

    @Override
    public void delete(Produto produto) {
        springDataRepository.delete(produto);
    }
}
