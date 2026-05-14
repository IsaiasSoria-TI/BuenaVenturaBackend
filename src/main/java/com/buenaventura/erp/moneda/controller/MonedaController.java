package com.buenaventura.erp.moneda.controller;

import com.buenaventura.erp.moneda.dto.MonedaResponse;
import com.buenaventura.erp.moneda.service.MonedaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/monedas")
public class MonedaController {

    private final MonedaService monedaService;

    public MonedaController(MonedaService monedaService) {
        this.monedaService = monedaService;
    }

    @GetMapping
    public List<MonedaResponse> listar() {
        return monedaService.listar();
    }
}
