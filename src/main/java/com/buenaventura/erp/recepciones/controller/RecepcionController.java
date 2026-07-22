package com.buenaventura.erp.recepciones.controller;

import com.buenaventura.erp.recepciones.dto.RecepcionDetalleResponse;
import com.buenaventura.erp.recepciones.dto.RecepcionDatosRequest;
import com.buenaventura.erp.recepciones.dto.RecepcionRequest;
import com.buenaventura.erp.recepciones.dto.RecepcionResponse;
import com.buenaventura.erp.recepciones.service.RecepcionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recepciones")
public class RecepcionController {

    private final RecepcionService recepcionService;

    public RecepcionController(RecepcionService recepcionService) {
        this.recepcionService = recepcionService;
    }

    @GetMapping
    public List<RecepcionResponse> listar() {
        return recepcionService.listar();
    }

    @PostMapping
    public RecepcionResponse registrar(@Valid @RequestBody RecepcionRequest request) {
        return recepcionService.registrar(request);
    }

    @PutMapping("/{id}/datos")
    public RecepcionResponse actualizarDatos(
            @PathVariable Integer id,
            @Valid @RequestBody RecepcionDatosRequest request
    ) {
        return recepcionService.actualizarDatos(id, request);
    }

    @GetMapping("/compras-pendientes")
    public List<RecepcionResponse> listarComprasPendientes() {
        return recepcionService.listarComprasPendientes();
    }

    @GetMapping("/detalle-compra/{idCompras}")
    public RecepcionDetalleResponse verDetalleCompra(@PathVariable Integer idCompras) {
        return recepcionService.verDetalleCompra(idCompras);
    }
}
