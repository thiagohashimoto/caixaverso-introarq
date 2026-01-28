package com.caixaverso.minierp.vendas.domain.service;

import com.caixaverso.minierp.vendas.domain.model.Venda;
import com.caixaverso.minierp.vendas.domain.repository.VendaRepository;
import com.caixaverso.minierp.vendas.domain.event.VendaCriadaEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Serviço de Domínio: CriarVendaService
 *
 * Conceito DDD: Domain Services contêm lógica de negócio que não
 * pertence naturalmente a uma entidade ou value object.
 *
 * Conceito SOLID: Single Responsibility - apenas criar venda.
 * Dependency Inversion - depende de abstrações (VendaRepository).
 */
@Service
@AllArgsConstructor
public class CriarVendaService {

    private final VendaRepository vendaRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Executa a criação de uma nova venda
     *
     * @param clienteId ID do cliente
     * @param produtoId ID do produto
     * @param quantidade Quantidade desejada
     * @param preco Preço unitário
     * @return Venda criada
     *
     * @throws IllegalArgumentException se parâmetros inválidos
     */
    public Venda executar(Long clienteId, Long produtoId, Integer quantidade, BigDecimal preco) {
        // 1. Validar comando
        validar(clienteId, produtoId, quantidade, preco);

        // 2. Criar agregado (entidade raiz)
        Venda venda = Venda.criar(clienteId);

        // 3. Adicionar item
        venda.adicionarItem(produtoId, quantidade, preco);

        // 4. Persistir
        vendaRepository.save(venda);

        // 5. Publicar evento de domínio (SAGA inicia aqui)
        VendaCriadaEvent event = new VendaCriadaEvent(
            venda.getId(),
            venda.getClienteId(),
            produtoId,
            quantidade,
            venda.getValorTotal()
        );
        eventPublisher.publishEvent(event);

        return venda;
    }

    private void validar(Long clienteId, Long produtoId, Integer quantidade, BigDecimal preco) {
        if (clienteId == null || clienteId <= 0) {
            throw new IllegalArgumentException("ClienteId inválido");
        }
        if (produtoId == null || produtoId <= 0) {
            throw new IllegalArgumentException("ProdutoId inválido");
        }
        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        if (preco == null || preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }
    }
}

