package com.caixaverso.minierp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Testes de Inicialização: MinierpApplicationTests
 *
 * Valida que a aplicação Spring Boot inicia corretamente.
 */
@SpringBootTest(classes = MinierpApplication.class)
@ActiveProfiles("test")
@DisplayName("Testes de Inicialização - MinierpApplication")
class MinierpApplicationTests {

    @Test
    @DisplayName("Deve iniciar aplicação com sucesso")
    void deveIniciarAplicacaoComSucesso() {
        // Apenas validar que o contexto Spring foi carregado
        // Se chegou aqui, a aplicação iniciou corretamente
    }
}
