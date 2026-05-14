package com.buenaventura.erp.proveedores.banco.service;

import com.buenaventura.erp.proveedores.banco.dto.BancoRequest;
import com.buenaventura.erp.proveedores.banco.dto.BancoResponse;

import java.util.List;

public interface BancoService {
    List<BancoResponse> listarActivos();

    List<BancoResponse> listarTodos();

    BancoResponse crear(BancoRequest request);

    BancoResponse actualizar(Integer id, BancoRequest request);

    void eliminar(Integer id);
}
