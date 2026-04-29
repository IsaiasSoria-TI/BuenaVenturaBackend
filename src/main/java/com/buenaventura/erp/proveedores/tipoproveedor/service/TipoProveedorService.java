package com.buenaventura.erp.proveedores.tipoproveedor.service;

import com.buenaventura.erp.proveedores.tipoproveedor.entity.TipoProveedor;

import java.util.List;

public interface TipoProveedorService {

    List<TipoProveedor> listarTodos();

    List<TipoProveedor> listarActivos();

    TipoProveedor crear(TipoProveedor request);

    TipoProveedor actualizar(Integer id, TipoProveedor request);

    void eliminar(Integer id);
}
