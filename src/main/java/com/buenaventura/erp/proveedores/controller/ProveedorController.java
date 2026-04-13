package com.buenaventura.erp.proveedores.controller;

import com.buenaventura.erp.proveedores.dto.ProveedorRequest;
import com.buenaventura.erp.proveedores.dto.ProveedorResponse;
import com.buenaventura.erp.proveedores.service.ProveedorService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping
    public List<ProveedorResponse> listar() {
        return proveedorService.listar();
    }

    @PostMapping
    public ProveedorResponse registrar(@Valid @RequestBody ProveedorRequest request) {
        return proveedorService.registrar(request);
    }

    @PutMapping("/{id}")
    public ProveedorResponse actualizar(@PathVariable Integer id,
                                        @Valid @RequestBody ProveedorRequest request) {
        return proveedorService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public void eliminarLogico(@PathVariable Integer id) {
        proveedorService.eliminarLogico(id);
    }
}