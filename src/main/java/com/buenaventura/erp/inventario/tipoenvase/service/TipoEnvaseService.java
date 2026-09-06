package com.buenaventura.erp.inventario.tipoenvase.service;

import com.buenaventura.erp.inventario.tipoenvase.dto.TipoEnvaseRequest;
import com.buenaventura.erp.inventario.tipoenvase.dto.TipoEnvaseResponse;

import java.util.List;

public interface TipoEnvaseService {

    List<TipoEnvaseResponse> listar();

    TipoEnvaseResponse crear(TipoEnvaseRequest request);

    TipoEnvaseResponse actualizar(Integer id, TipoEnvaseRequest request);

    void eliminar(Integer id);
}
