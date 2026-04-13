package com.buenaventura.erp.proveedores.service;

import com.buenaventura.erp.proveedores.dto.ProveedorRequest;
import com.buenaventura.erp.proveedores.dto.ProveedorResponse;

import java.util.List;

public interface ProveedorService {

    List<ProveedorResponse> listar();

    ProveedorResponse registrar(ProveedorRequest request);

    ProveedorResponse actualizar(Integer id, ProveedorRequest request);

    void eliminarLogico(Integer id);
}