package com.buenaventura.erp.proveedores.tipoproveedor.repository;

import com.buenaventura.erp.proveedores.tipoproveedor.entity.TipoProveedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipoProveedorRepository extends JpaRepository<TipoProveedor, Integer> {

    List<TipoProveedor> findByFlgActivoTrue();
}