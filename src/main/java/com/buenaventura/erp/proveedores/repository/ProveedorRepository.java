package com.buenaventura.erp.proveedores.repository;

import com.buenaventura.erp.proveedores.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {

    List<Proveedor> findByFlgActivoTrue();

    Optional<Proveedor> findByRuc(String ruc);

    boolean existsByRuc(String ruc);
}