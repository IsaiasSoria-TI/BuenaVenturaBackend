package com.buenaventura.erp.tipocambio.controller;

import com.buenaventura.erp.tipocambio.dto.TipoCambioRequest;
import com.buenaventura.erp.tipocambio.dto.TipoCambioResponse;
import com.buenaventura.erp.tipocambio.service.TipoCambioService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tipos-cambio")
public class TipoCambioController {

    private final TipoCambioService tipoCambioService;

    public TipoCambioController(TipoCambioService tipoCambioService) {
        this.tipoCambioService = tipoCambioService;
    }

    @GetMapping
    public List<TipoCambioResponse> listar() {
        return tipoCambioService.listar();
    }

    @GetMapping("/todos")
    public List<TipoCambioResponse> listarTodos() {
        return tipoCambioService.listarTodos();
    }

    @GetMapping("/aplicable")
    public TipoCambioResponse buscarAplicable(
            @RequestParam("fecha")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha
    ) {
        return tipoCambioService.buscarAplicable(fecha);
    }

    @PostMapping
    public TipoCambioResponse crear(@Valid @RequestBody TipoCambioRequest request) {
        return tipoCambioService.crear(request);
    }

    @PutMapping("/{id}")
    public TipoCambioResponse actualizar(@PathVariable Integer id, @Valid @RequestBody TipoCambioRequest request) {
        return tipoCambioService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        tipoCambioService.eliminar(id);
    }
}
