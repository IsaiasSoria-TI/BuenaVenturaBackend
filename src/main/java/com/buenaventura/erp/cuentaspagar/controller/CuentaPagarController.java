package com.buenaventura.erp.cuentaspagar.controller;

import com.buenaventura.erp.cuentaspagar.dto.CuentaPagarDisponibleResponse;
import com.buenaventura.erp.cuentaspagar.dto.CuentaPagarRequest;
import com.buenaventura.erp.cuentaspagar.entity.CuentaPagar;
import com.buenaventura.erp.cuentaspagar.service.CuentaPagarService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuentas-pagar")
public class CuentaPagarController {

    private final CuentaPagarService cuentaPagarService;

    public CuentaPagarController(CuentaPagarService cuentaPagarService) {
        this.cuentaPagarService = cuentaPagarService;
    }

    @GetMapping
    public ResponseEntity<List<CuentaPagar>> listar() {
        return ResponseEntity.ok(cuentaPagarService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaPagar> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(cuentaPagarService.obtenerPorId(id));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<CuentaPagarDisponibleResponse>> listarDisponibles() {
        return ResponseEntity.ok(cuentaPagarService.listarDisponibles());
    }

    @PostMapping
    public ResponseEntity<List<CuentaPagar>> registrar(@Valid @RequestBody CuentaPagarRequest request) {
        return ResponseEntity.ok(cuentaPagarService.registrar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuentaPagar> actualizar(@PathVariable Integer id,
                                                  @Valid @RequestBody CuentaPagarRequest request) {
        return ResponseEntity.ok(cuentaPagarService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        cuentaPagarService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}