package com.buenaventura.erp.compras.controller;

import com.buenaventura.erp.compras.dto.CompraRequest;
import com.buenaventura.erp.compras.dto.CompraResponse;
import com.buenaventura.erp.compras.service.CompraService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compras")
public class CompraController {

    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @GetMapping
    public List<CompraResponse> listar() {
        return compraService.listar();
    }

    @PostMapping
    public CompraResponse registrar(@Valid @RequestBody CompraRequest request) {
        return compraService.registrar(request);
    }

    @PutMapping("/{id}")
    public CompraResponse actualizar(@PathVariable Integer id,
                                     @Valid @RequestBody CompraRequest request) {
        return compraService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public void eliminarLogico(@PathVariable Integer id) {
        compraService.eliminarLogico(id);
    }
}