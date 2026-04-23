package com.buenaventura.erp.cuentacontable.controller;

import com.buenaventura.erp.cuentacontable.dto.CuentaContableRequest;
import com.buenaventura.erp.cuentacontable.dto.CuentaContableResponse;
import com.buenaventura.erp.cuentacontable.service.CuentaContableService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas-contables")
public class CuentaContableController {

    private final CuentaContableService service;

    public CuentaContableController(CuentaContableService service) {
        this.service = service;
    }

    @GetMapping
    public List<CuentaContableResponse> listar() {
        return service.listar();
    }

    @PostMapping
    public CuentaContableResponse crear(@RequestBody CuentaContableRequest request) {
        return service.crear(request);
    }

    @PutMapping("/{id}")
    public CuentaContableResponse actualizar(
            @PathVariable Integer id,
            @RequestBody CuentaContableRequest request
    ) {
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        service.eliminar(id);
    }
}