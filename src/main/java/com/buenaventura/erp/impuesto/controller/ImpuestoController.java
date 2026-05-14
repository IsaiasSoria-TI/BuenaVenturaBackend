package com.buenaventura.erp.impuesto.controller;

import com.buenaventura.erp.impuesto.dto.ImpuestoRequest;
import com.buenaventura.erp.impuesto.dto.ImpuestoResponse;
import com.buenaventura.erp.impuesto.service.ImpuestoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/impuestos")
public class ImpuestoController {

    private final ImpuestoService impuestoService;

    public ImpuestoController(ImpuestoService impuestoService) {
        this.impuestoService = impuestoService;
    }

    @GetMapping
    public List<ImpuestoResponse> listar() {
        return impuestoService.listar();
    }

    @GetMapping("/todos")
    public List<ImpuestoResponse> listarTodos() {
        return impuestoService.listarTodos();
    }

    @PostMapping
    public ImpuestoResponse crear(@Valid @RequestBody ImpuestoRequest request) {
        return impuestoService.crear(request);
    }

    @PutMapping("/{id}")
    public ImpuestoResponse actualizar(@PathVariable Integer id, @Valid @RequestBody ImpuestoRequest request) {
        return impuestoService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        impuestoService.eliminar(id);
    }
}
