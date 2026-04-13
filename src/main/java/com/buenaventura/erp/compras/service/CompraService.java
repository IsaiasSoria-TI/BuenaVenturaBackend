package com.buenaventura.erp.compras.service;

import com.buenaventura.erp.compras.dto.CompraRequest;
import com.buenaventura.erp.compras.dto.CompraResponse;

import java.util.List;

public interface CompraService {

    List<CompraResponse> listar();

    CompraResponse registrar(CompraRequest request);

    CompraResponse actualizar(Integer id, CompraRequest request);

    void eliminarLogico(Integer id);
}