package com.buenaventura.erp.inventario.consultastock.service;

import com.buenaventura.erp.inventario.consultastock.dto.HistorialMovimientoResponse;
import com.buenaventura.erp.inventario.consultastock.dto.MovimientoManualRequest;

import java.util.List;

public interface HistorialMovimientoService {

    List<HistorialMovimientoResponse> buscar(String periodo, Integer idArticulo, String busqueda);

    HistorialMovimientoResponse registrarManual(MovimientoManualRequest request);
}
