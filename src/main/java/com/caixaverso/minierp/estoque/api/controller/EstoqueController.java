package com.caixaverso.minierp.estoque.api.controller;

import com.caixaverso.minierp.estoque.application.service.EstoqueApplicationService;
import com.caixaverso.minierp.estoque.application.dto.ProdutoResponseDTO;
import com.caixaverso.minierp.estoque.application.dto.CriarProdutoDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controller: EstoqueController
 */
@RestController
@RequestMapping("/estoque")
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class EstoqueController {

    private final EstoqueApplicationService estoqueApplicationService;

    /**
     * POST /estoque
     * Cria um novo produto
     */
    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> criar(@Valid @RequestBody CriarProdutoDTO dto) {
        ProdutoResponseDTO produto = estoqueApplicationService.criarProduto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(produto);
    }

    /**
     * GET /estoque/{produtoId}
     * Obtém um produto por ID
     */
    @GetMapping("/{produtoId}")
    public ResponseEntity<ProdutoResponseDTO> obter(@PathVariable Long produtoId) {
        ProdutoResponseDTO produto = estoqueApplicationService.obter(produtoId);
        return ResponseEntity.ok(produto);
    }

    /**
     * GET /estoque
     * Lista todos os produtos
     */
    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listar() {
        List<ProdutoResponseDTO> produtos = estoqueApplicationService.listar();
        return ResponseEntity.ok(produtos);
    }
}

