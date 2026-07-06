package com.buenaventura.erp.inventario.consultastock.service;

import com.buenaventura.erp.inventario.consultastock.dto.MotivoMovimientoKardexResponse;

import java.util.List;

public interface MotivoMovimientoKardexService {

    List<MotivoMovimientoKardexResponse> listarActivos();
}
