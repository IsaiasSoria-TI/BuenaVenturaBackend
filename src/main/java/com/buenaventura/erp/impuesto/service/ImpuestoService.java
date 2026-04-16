package com.buenaventura.erp.impuesto.service;

import com.buenaventura.erp.impuesto.dto.ImpuestoResponse;

import java.util.List;

public interface ImpuestoService {
    List<ImpuestoResponse> listar();
}