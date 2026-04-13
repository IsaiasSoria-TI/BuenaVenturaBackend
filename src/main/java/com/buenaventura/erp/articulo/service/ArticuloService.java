package com.buenaventura.erp.articulo.service;

import com.buenaventura.erp.articulo.dto.ArticuloRequest;
import com.buenaventura.erp.articulo.dto.ArticuloResponse;

import java.util.List;

public interface ArticuloService {

    List<ArticuloResponse> listar();

    ArticuloResponse registrar(ArticuloRequest request);

    ArticuloResponse actualizar(Integer id, ArticuloRequest request);

    void eliminarLogico(Integer id);
}