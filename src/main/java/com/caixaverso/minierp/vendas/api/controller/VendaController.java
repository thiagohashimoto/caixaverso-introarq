package com.caixaverso.minierp.vendas.api.controller;

import com.caixaverso.minierp.vendas.application.service.VendaApplicationService;
import com.caixaverso.minierp.vendas.application.dto.CriarVendaDTO;
import com.caixaverso.minierp.vendas.application.dto.VendaResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controller: VendaController
 *
 * Conceito SOLID: Single Responsibility - apenas rotear requisições HTTP.
 * Não contém lógica de negócio, apenas orquestra chamadas a services.
 */
@RestController
@RequestMapping("/vendas")
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class VendaController {

    private final VendaApplicationService vendaApplicationService;

    /**
     * POST /vendas
     * Cria uma nova venda
     */
    @PostMapping
    public ResponseEntity<VendaResponseDTO> criar(@Valid @RequestBody CriarVendaDTO dto) {
        VendaResponseDTO venda = vendaApplicationService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(venda);
    }

    /**
     * GET /vendas/{id}
     * Obtém uma venda por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<VendaResponseDTO> obter(@PathVariable Long id) {
        VendaResponseDTO venda = vendaApplicationService.obter(id);
        return ResponseEntity.ok(venda);
    }

    /**
     * GET /vendas
     * Lista todas as vendas
     */
    @GetMapping
    public ResponseEntity<List<VendaResponseDTO>> listar() {
        List<VendaResponseDTO> vendas = vendaApplicationService.listar();
        return ResponseEntity.ok(vendas);
    }

    /**
     * GET /vendas/cliente/{clienteId}
     * Lista vendas de um cliente específico
     */
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<VendaResponseDTO>> listarPorCliente(@PathVariable Long clienteId) {
        List<VendaResponseDTO> vendas = vendaApplicationService.listarPorCliente(clienteId);
        return ResponseEntity.ok(vendas);
    }

    /**
     * PUT /vendas/{id}/confirmar
     * Confirma uma venda (chamado pela SAGA após sucesso de estoque e cobrança)
     */
    @PutMapping("/{id}/confirmar")
    public ResponseEntity<VendaResponseDTO> confirmar(@PathVariable Long id) {
        VendaResponseDTO venda = vendaApplicationService.confirmar(id);
        return ResponseEntity.ok(venda);
    }

    /**
     * PUT /vendas/{id}/cancelar
     * Cancela uma venda
     */
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        vendaApplicationService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}
