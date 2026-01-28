package com.caixaverso.minierp.estoque.domain.service;

import com.caixaverso.minierp.estoque.domain.model.Produto;
import com.caixaverso.minierp.estoque.domain.repository.ProdutoRepository;
import com.caixaverso.minierp.estoque.domain.event.EstoqueReservadoEvent;
import com.caixaverso.minierp.estoque.domain.event.EstoqueInsuficienteEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * Serviço de Domínio: ReservarEstoqueService
 *
 * Responsável por reservar estoque quando uma venda é criada.
 * Parte do padrão SAGA - reage ao evento VendaCriadaEvent.
 */
@Service
@AllArgsConstructor
public class ReservarEstoqueService {

    private final ProdutoRepository produtoRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Executa a reserva de estoque
     */
    public void executar(Long vendaId, Long produtoId, Integer quantidade) {
        Produto produto = produtoRepository.findById(produtoId)
            .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + produtoId));

        // Verificar disponibilidade
        if (!produto.temDisponibilidade(quantidade)) {
            // Publicar evento de compensação
            EstoqueInsuficienteEvent event = new EstoqueInsuficienteEvent(
                vendaId,
                produtoId,
                quantidade,
                produto.getQuantidadeDisponivel() - produto.getQuantidadeReservada()
            );
            eventPublisher.publishEvent(event);
            return;
        }

        // Reservar estoque
        produto.reservar(quantidade);
        produtoRepository.save(produto);

        // Publicar evento de sucesso
        EstoqueReservadoEvent event = new EstoqueReservadoEvent(
            vendaId,
            produtoId,
            quantidade
        );
        eventPublisher.publishEvent(event);
    }
}

