package com.buenaventura.erp.cuentaspagar.service;

import com.buenaventura.erp.cuentaspagar.dto.CompraValidaResponse;
import com.buenaventura.erp.cuentaspagar.dto.CuentaPagarDetalleCompraResponse;
import com.buenaventura.erp.cuentaspagar.dto.CuentaPagarRequest;
import com.buenaventura.erp.cuentaspagar.dto.CuentaPagarResponse;

import java.util.List;

public interface CuentaPagarService {

    List<CuentaPagarResponse> listar();

    List<CompraValidaResponse> listarComprasValidas();

    CuentaPagarDetalleCompraResponse verDetalleCompra(Integer idCompras);

    List<CuentaPagarResponse> registrar(CuentaPagarRequest request);

    CuentaPagarResponse actualizar(Integer id, CuentaPagarRequest request);

    void eliminar(Integer id);
}