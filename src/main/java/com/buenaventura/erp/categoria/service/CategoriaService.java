package com.buenaventura.erp.categoria.service;

import com.buenaventura.erp.categoria.dto.CategoriaRequest;
import com.buenaventura.erp.categoria.dto.CategoriaResponse;

import java.util.List;

public interface CategoriaService {

    List<CategoriaResponse> listar();

    CategoriaResponse crear(CategoriaRequest request);

    CategoriaResponse actualizar(Integer id, CategoriaRequest request);

    void eliminar(Integer id);
}