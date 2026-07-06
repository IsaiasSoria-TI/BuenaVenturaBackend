package com.buenaventura.erp.inventario.consultastock.controller;

import com.buenaventura.erp.inventario.consultastock.dto.HistorialMovimientoResponse;
import com.buenaventura.erp.inventario.consultastock.dto.MovimientoManualRequest;
import com.buenaventura.erp.inventario.consultastock.service.HistorialMovimientoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inventario/historial-movimientos")
public class HistorialMovimientoController {

    private final HistorialMovimientoService historialMovimientoService;

    public HistorialMovimientoController(HistorialMovimientoService historialMovimientoService) {
        this.historialMovimientoService = historialMovimientoService;
    }

    @GetMapping
    public List<HistorialMovimientoResponse> buscar(
            @RequestParam(required = false) String periodo,
            @RequestParam(required = false) Integer idArticulo,
            @RequestParam(required = false) String busqueda
    ) {
        return historialMovimientoService.buscar(periodo, idArticulo, busqueda);
    }

    @PostMapping("/manual")
    public HistorialMovimientoResponse registrarManual(@Valid @RequestBody MovimientoManualRequest request) {
        return historialMovimientoService.registrarManual(request);
    }
}
