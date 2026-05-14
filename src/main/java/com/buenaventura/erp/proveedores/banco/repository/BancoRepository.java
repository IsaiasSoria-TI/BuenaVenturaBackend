package com.buenaventura.erp.proveedores.banco.repository;

import com.buenaventura.erp.proveedores.banco.entity.Banco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BancoRepository extends JpaRepository<Banco, Integer> {
    List<Banco> findByFlgActivoTrueOrderByNombreAsc();

    List<Banco> findAllByOrderByNombreAsc();

    Optional<Banco> findByNombreIgnoreCase(String nombre);
}
