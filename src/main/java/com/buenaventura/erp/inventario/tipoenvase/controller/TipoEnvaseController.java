package com.buenaventura.erp.inventario.tipoenvase.controller;

import com.buenaventura.erp.inventario.tipoenvase.dto.TipoEnvaseRequest;
import com.buenaventura.erp.inventario.tipoenvase.dto.TipoEnvaseResponse;
import com.buenaventura.erp.inventario.tipoenvase.service.TipoEnvaseService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario/tipos-envase")
public class TipoEnvaseController {

    private final TipoEnvaseService service;

    public TipoEnvaseController(TipoEnvaseService service) {
        this.service = service;
    }

    @GetMapping
    public List<TipoEnvaseResponse> listar() {
        return service.listar();
    }

    @PostMapping
    public TipoEnvaseResponse crear(@Valid @RequestBody TipoEnvaseRequest request) {
        return service.crear(request);
    }

    @PutMapping("/{id}")
    public TipoEnvaseResponse actualizar(@PathVariable Integer id,
                                          @Valid @RequestBody TipoEnvaseRequest request) {
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        service.eliminar(id);
    }
}
