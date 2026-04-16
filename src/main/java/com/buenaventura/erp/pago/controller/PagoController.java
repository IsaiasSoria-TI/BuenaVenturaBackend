package com.buenaventura.erp.pago.controller;

import com.buenaventura.erp.pago.dto.PagoResponse;
import com.buenaventura.erp.pago.service.PagoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping
    public List<PagoResponse> listar() {
        return pagoService.listar();
    }
}