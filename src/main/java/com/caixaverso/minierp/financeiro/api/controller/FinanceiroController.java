package com.caixaverso.minierp.financeiro.api.controller;

import com.caixaverso.minierp.financeiro.application.service.FinanceiroApplicationService;
import com.caixaverso.minierp.financeiro.application.dto.CobrancaResponseDTO;
import com.caixaverso.minierp.financeiro.application.dto.CriarCobrancaDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controller: FinanceiroController
 *
 * Conceito SOLID: Single Responsibility - apenas rotear requisições HTTP.
 * Não contém lógica de negócio, apenas orquestra chamadas a services.
 */
@RestController
@RequestMapping("/financeiro/cobrancas")
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class FinanceiroController {

    private final FinanceiroApplicationService financeiroApplicationService;

    /**
     * GET /financeiro/cobrancas
     * Lista todas as cobranças
     */
    @GetMapping
    public ResponseEntity<List<CobrancaResponseDTO>> listar() {
        List<CobrancaResponseDTO> cobancas = financeiroApplicationService.listar();
        return ResponseEntity.ok(cobancas);
    }

    /**
     * POST /financeiro/cobrancas
     * Cria uma nova cobrança
     *
     * @param dto Dados para criação da cobrança
     * @return Cobrança criada com status 201
     */
    @PostMapping
    public ResponseEntity<CobrancaResponseDTO> criar(@RequestBody @Valid CriarCobrancaDTO dto) {
        CobrancaResponseDTO cobranca = financeiroApplicationService.criarCobranca(
            dto.getVendaId(),
            dto.getClienteId(),
            dto.getValor()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(cobranca);
    }

    /**
     * GET /financeiro/cobrancas/{id}
     * Obtém uma cobrança por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<CobrancaResponseDTO> obter(@PathVariable Long id) {
        CobrancaResponseDTO cobranca = financeiroApplicationService.obter(id);
        return ResponseEntity.ok(cobranca);
    }

    /**
     * GET /financeiro/cobrancas/cliente/{clienteId}
     * Lista cobranças de um cliente específico
     */
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<CobrancaResponseDTO>> listarPorCliente(@PathVariable Long clienteId) {
        List<CobrancaResponseDTO> cobrancas = financeiroApplicationService.listarPorCliente(clienteId);
        return ResponseEntity.ok(cobrancas);
    }

    /**
     * PUT /financeiro/cobrancas/{id}/pagar
     * Registra o pagamento de uma cobrança
     */
    @PutMapping("/{id}/pagar")
    public ResponseEntity<CobrancaResponseDTO> pagar(@PathVariable Long id) {
        CobrancaResponseDTO cobranca = financeiroApplicationService.pagar(id);
        return ResponseEntity.ok(cobranca);
    }
}

