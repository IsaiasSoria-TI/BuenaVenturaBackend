package com.buenaventura.erp.proveedores.banco.repository;

import com.buenaventura.erp.proveedores.banco.entity.BancoProveedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BancoProveedorRepository extends JpaRepository<BancoProveedor, Integer> {
    Optional<BancoProveedor> findByIdProveedorAndFlgActivo(Integer idProveedor, Boolean flgActivo);

    Optional<BancoProveedor> findFirstByCuentaBancariaAndFlgActivoTrue(String cuentaBancaria);

    Optional<BancoProveedor> findFirstByCuentaInterbancariaAndFlgActivoTrue(String cuentaInterbancaria);
}
