package com.buenaventura.erp.impuesto.service;

import com.buenaventura.erp.impuesto.dto.ImpuestoRequest;
import com.buenaventura.erp.impuesto.dto.ImpuestoResponse;

import java.util.List;

public interface ImpuestoService {
    List<ImpuestoResponse> listar();

    List<ImpuestoResponse> listarTodos();

    ImpuestoResponse crear(ImpuestoRequest request);

    ImpuestoResponse actualizar(Integer id, ImpuestoRequest request);

    void eliminar(Integer id);
}
