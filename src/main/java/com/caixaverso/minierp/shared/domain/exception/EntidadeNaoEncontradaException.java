package com.caixaverso.minierp.shared.domain.exception;

/**
 * Exceção lançada quando uma entidade não é encontrada.
 */
public class EntidadeNaoEncontradaException extends DomainException {

    public EntidadeNaoEncontradaException(String tipo, Long id) {
        super("ENTIDADE_NAO_ENCONTRADA",
              String.format("%s com ID %d não encontrado", tipo, id));
    }

    public EntidadeNaoEncontradaException(String tipo, String id) {
        super("ENTIDADE_NAO_ENCONTRADA",
              String.format("%s com ID %s não encontrado", tipo, id));
    }
}

