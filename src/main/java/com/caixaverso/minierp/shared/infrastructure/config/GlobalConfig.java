package com.caixaverso.minierp.shared.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configuração Global: GlobalConfig
 *
 * Habilita gerenciamento de transações em toda a aplicação.
 */
@Configuration
@EnableTransactionManagement
public class GlobalConfig {
}

