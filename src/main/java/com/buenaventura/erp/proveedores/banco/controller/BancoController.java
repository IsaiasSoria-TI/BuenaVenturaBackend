package com.buenaventura.erp.proveedores.banco.controller;

import com.buenaventura.erp.proveedores.banco.dto.BancoRequest;
import com.buenaventura.erp.proveedores.banco.dto.BancoResponse;
import com.buenaventura.erp.proveedores.banco.service.BancoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bancos")
public class BancoController {

    private final BancoService bancoService;

    public BancoController(BancoService bancoService) {
        this.bancoService = bancoService;
    }

    @GetMapping
    public List<BancoResponse> listar() {
        return bancoService.listarActivos();
    }

    @GetMapping("/todos")
    public List<BancoResponse> listarTodos() {
        return bancoService.listarTodos();
    }

    @PostMapping
    public BancoResponse crear(@Valid @RequestBody BancoRequest request) {
        return bancoService.crear(request);
    }

    @PutMapping("/{id}")
    public BancoResponse actualizar(@PathVariable Integer id, @Valid @RequestBody BancoRequest request) {
        return bancoService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        bancoService.eliminar(id);
    }
}
