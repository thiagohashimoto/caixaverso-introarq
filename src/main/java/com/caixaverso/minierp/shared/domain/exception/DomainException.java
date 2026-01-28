package com.caixaverso.minierp.shared.domain.exception;

/**
 * Exceção base para todas as exceções de domínio.
 *
 * Conceito SOLID: Permite tratamento centralizado de erros de negócio.
 */
public class DomainException extends RuntimeException {

    private final String code;

    public DomainException(String message) {
        super(message);
        this.code = "DOMAIN_ERROR";
    }

    public DomainException(String code, String message) {
        super(message);
        this.code = code;
    }

    public DomainException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

