package com.caixaverso.minierp.shared.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuração de Segurança: WebSecurityConfig
 *
 * Responsabilidades:
 * - Permitir acesso ao H2 Console
 * - Desabilitar CSRF para desenvolvimento (H2 Console e endpoints da API)
 * - Permitir frames para H2 Console
 *
 * Notas:
 * - H2 Console é uma ferramenta de desenvolvimento
 * - CSRF está desabilitado para facilitar desenvolvimento e testes
 * - NUNCA deve ser usado em ambiente de PRODUÇÃO sem habilitar CSRF e autenticação adequada
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                // H2 Console - Permitir acesso sem autenticação (DESENVOLVIMENTO APENAS)
                .requestMatchers("/h2-console", "/h2-console/**").permitAll()
                // Outros endpoints da API - Permitir por enquanto (sem autenticação)
                .anyRequest().permitAll()
            )
            // Desabilitar CSRF para desenvolvimento (H2 Console e API endpoints)
            // NOTA: Em produção, habilitar CSRF e usar tokens apropriados
            .csrf(csrf -> csrf.disable())
            // Headers - Permitir frames para H2 Console
            .headers(headers -> headers
                .frameOptions(frame -> frame.disable())
            );

        return http.build();
    }
}
