package com.buenaventura.erp.tipocambio.service;

import com.buenaventura.erp.tipocambio.dto.TipoCambioRequest;
import com.buenaventura.erp.tipocambio.dto.TipoCambioResponse;

import java.time.LocalDate;
import java.util.List;

public interface TipoCambioService {
    List<TipoCambioResponse> listar();

    List<TipoCambioResponse> listarTodos();

    TipoCambioResponse crear(TipoCambioRequest request);

    TipoCambioResponse actualizar(Integer id, TipoCambioRequest request);

    void eliminar(Integer id);

    TipoCambioResponse buscarAplicable(LocalDate fecha);
}
