package com.caixaverso.minierp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Classe Principal: MinierpApplication
 *
 * Ponto de entrada da aplicação Spring Boot.
 * Habilita componentes de todos os módulos (Vendas, Estoque, Financeiro, Shared).
 */
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.caixaverso.minierp.vendas",
    "com.caixaverso.minierp.estoque",
    "com.caixaverso.minierp.financeiro",
    "com.caixaverso.minierp.shared",
    "com.caixaverso.minierp.common"
})
@EnableJpaRepositories(basePackages = {
    "com.caixaverso.minierp.vendas.infrastructure.persistence",
    "com.caixaverso.minierp.estoque.infrastructure.persistence",
    "com.caixaverso.minierp.financeiro.infrastructure.persistence"
})
@EnableAsync
public class MinierpApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinierpApplication.class, args);
    }
}
