package com.caixaverso.minierp.estoque.infrastructure.persistence;

import com.caixaverso.minierp.estoque.domain.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Repository para Produto
 */
@Repository
public interface ProdutoSpringDataRepository extends JpaRepository<Produto, Long> {
}

