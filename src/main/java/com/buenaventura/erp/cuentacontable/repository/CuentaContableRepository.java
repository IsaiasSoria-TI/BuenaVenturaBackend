package com.buenaventura.erp.cuentacontable.repository;

import com.buenaventura.erp.cuentacontable.entity.CuentaContable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CuentaContableRepository extends JpaRepository<CuentaContable, Integer> {

    List<CuentaContable> findByEstado(String estado);
}