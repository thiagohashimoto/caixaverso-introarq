package com.caixaverso.minierp.financeiro.domain.service;

import com.caixaverso.minierp.financeiro.domain.model.Cobranca;
import com.caixaverso.minierp.financeiro.domain.repository.CobrancaRepository;
import com.caixaverso.minierp.financeiro.domain.event.CobrancaCriadaEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Serviço de Domínio: CriarCobrancaService
 *
 * Responsável por criar cobrança quando estoque é reservado.
 * Parte do padrão SAGA - reage ao evento EstoqueReservadoEvent.
 */
@Service
@AllArgsConstructor
public class CriarCobrancaService {

    private final CobrancaRepository cobrancaRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Executa a criação de cobrança
     */
    public void executar(Long vendaId, Long clienteId, BigDecimal valor) {
        // 1. Validar
        if (vendaId == null || vendaId <= 0) {
            throw new IllegalArgumentException("VendaId inválido");
        }
        if (clienteId == null || clienteId <= 0) {
            throw new IllegalArgumentException("ClienteId inválido");
        }
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero");
        }

        // 2. Criar cobrança
        Cobranca cobranca = Cobranca.criar(vendaId, clienteId, valor);

        // 3. Persistir
        cobrancaRepository.save(cobranca);

        // 4. Publicar evento (continua SAGA)
        CobrancaCriadaEvent event = new CobrancaCriadaEvent(
            cobranca.getId(),
            cobranca.getVendaId(),
            cobranca.getClienteId(),
            cobranca.getValor()
        );
        eventPublisher.publishEvent(event);
    }
}

