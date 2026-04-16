package com.buenaventura.erp.impuesto.controller;

import com.buenaventura.erp.impuesto.dto.ImpuestoResponse;
import com.buenaventura.erp.impuesto.service.ImpuestoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/impuestos")
public class ImpuestoController {

    private final ImpuestoService impuestoService;

    public ImpuestoController(ImpuestoService impuestoService) {
        this.impuestoService = impuestoService;
    }

    @GetMapping
    public List<ImpuestoResponse> listar() {
        return impuestoService.listar();
    }
}