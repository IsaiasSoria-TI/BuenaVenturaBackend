package com.buenaventura.erp.inventario.consultastock.service;

import com.buenaventura.erp.inventario.consultastock.dto.ConsultaStockMovimientoResponse;

import java.util.List;

public interface ConsultaStockService {

    List<ConsultaStockMovimientoResponse> consultar(String periodo, Integer idArticulo);
}
