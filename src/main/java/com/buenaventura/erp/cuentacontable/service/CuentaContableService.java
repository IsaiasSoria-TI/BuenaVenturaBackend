package com.buenaventura.erp.cuentacontable.service;

import com.buenaventura.erp.cuentacontable.dto.CuentaContableRequest;
import com.buenaventura.erp.cuentacontable.dto.CuentaContableResponse;

import java.util.List;

public interface CuentaContableService {

    List<CuentaContableResponse> listar();

    CuentaContableResponse crear(CuentaContableRequest request);

    CuentaContableResponse actualizar(Integer id, CuentaContableRequest request);

    void eliminar(Integer id);
}