package com.caixaverso.minierp.common.constant;

/**
 * Constantes da Aplicação
 */
public final class AppConstants {

    private AppConstants() {
        throw new AssertionError("Cannot instantiate AppConstants");
    }

    // Moeda padrão
    public static final String MOEDA_PADRAO = "BRL";

    // Mensagens de erro comuns
    public static final String ENTIDADE_NAO_ENCONTRADA = "Entidade não encontrada";
    public static final String PARAMETRO_INVALIDO = "Parâmetro inválido";
    public static final String OPERACAO_NAO_PERMITIDA = "Operação não permitida";

    // Timeouts
    public static final long SAGA_TIMEOUT_SEGUNDOS = 300;

    // Limites
    public static final int MAX_ITENS_VENDA = 100;
    public static final int QUANTIDADE_MINIMA = 1;
}
