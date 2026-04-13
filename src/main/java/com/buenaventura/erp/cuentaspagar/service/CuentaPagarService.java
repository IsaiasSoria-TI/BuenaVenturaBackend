package com.buenaventura.erp.cuentaspagar.service;

import com.buenaventura.erp.cuentaspagar.dto.CuentaPagarDisponibleResponse;
import com.buenaventura.erp.cuentaspagar.dto.CuentaPagarRequest;
import com.buenaventura.erp.cuentaspagar.entity.CuentaPagar;

import java.util.List;

public interface CuentaPagarService {

    List<CuentaPagar> listar();

    CuentaPagar obtenerPorId(Integer id);

    List<CuentaPagarDisponibleResponse> listarDisponibles();

    List<CuentaPagar> registrar(CuentaPagarRequest request);

    CuentaPagar actualizar(Integer id, CuentaPagarRequest request);

    void eliminar(Integer id);
}