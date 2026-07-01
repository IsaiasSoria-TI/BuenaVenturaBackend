package com.buenaventura.erp.inventario.consultastock.controller;

import com.buenaventura.erp.inventario.consultastock.dto.ConsultaStockMovimientoResponse;
import com.buenaventura.erp.inventario.consultastock.service.ConsultaStockService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inventario/consulta-stock")
public class ConsultaStockController {

    private final ConsultaStockService consultaStockService;

    public ConsultaStockController(ConsultaStockService consultaStockService) {
        this.consultaStockService = consultaStockService;
    }

    @GetMapping
    public List<ConsultaStockMovimientoResponse> consultar(
            @RequestParam String periodo,
            @RequestParam Integer idArticulo
    ) {
        return consultaStockService.consultar(periodo, idArticulo);
    }
}
