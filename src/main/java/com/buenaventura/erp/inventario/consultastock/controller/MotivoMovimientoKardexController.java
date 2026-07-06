package com.buenaventura.erp.inventario.consultastock.controller;

import com.buenaventura.erp.inventario.consultastock.dto.MotivoMovimientoKardexResponse;
import com.buenaventura.erp.inventario.consultastock.service.MotivoMovimientoKardexService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inventario/motivos-movimiento")
public class MotivoMovimientoKardexController {

    private final MotivoMovimientoKardexService motivoService;

    public MotivoMovimientoKardexController(MotivoMovimientoKardexService motivoService) {
        this.motivoService = motivoService;
    }

    @GetMapping
    public List<MotivoMovimientoKardexResponse> listarActivos() {
        return motivoService.listarActivos();
    }
}
