package com.buenaventura.erp.cuentaspagar.controller;

import com.buenaventura.erp.cuentaspagar.dto.CompraValidaResponse;
import com.buenaventura.erp.cuentaspagar.dto.CuentaPagarDetalleCompraResponse;
import com.buenaventura.erp.cuentaspagar.dto.CuentaPagarRequest;
import com.buenaventura.erp.cuentaspagar.dto.CuentaPagarResponse;
import com.buenaventura.erp.cuentaspagar.service.CuentaPagarService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas-pagar")
public class CuentaPagarController {

    private final CuentaPagarService cuentaPagarService;

    public CuentaPagarController(CuentaPagarService cuentaPagarService) {
        this.cuentaPagarService = cuentaPagarService;
    }

    @GetMapping
    public ResponseEntity<List<CuentaPagarResponse>> listar() {
        return ResponseEntity.ok(cuentaPagarService.listar());
    }

    @GetMapping("/compras-validas")
    public ResponseEntity<List<CompraValidaResponse>> listarComprasValidas() {
        return ResponseEntity.ok(cuentaPagarService.listarComprasValidas());
    }

    @GetMapping("/compra/{idCompras}")
    public ResponseEntity<CuentaPagarDetalleCompraResponse> verDetalleCompra(
            @PathVariable Integer idCompras
    ) {
        return ResponseEntity.ok(cuentaPagarService.verDetalleCompra(idCompras));
    }

    @PostMapping
    public ResponseEntity<List<CuentaPagarResponse>> registrar(
            @Valid @RequestBody CuentaPagarRequest request
    ) {
        return ResponseEntity.ok(cuentaPagarService.registrar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuentaPagarResponse> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody CuentaPagarRequest request
    ) {
        return ResponseEntity.ok(cuentaPagarService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        cuentaPagarService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}