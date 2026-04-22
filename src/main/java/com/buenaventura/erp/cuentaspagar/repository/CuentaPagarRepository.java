package com.buenaventura.erp.cuentaspagar.repository;

import com.buenaventura.erp.cuentaspagar.entity.CuentaPagar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CuentaPagarRepository extends JpaRepository<CuentaPagar, Integer> {

    List<CuentaPagar> findByFlgActivoTrueOrderByFechaCreacionDesc();

    Optional<CuentaPagar> findByIdCuentaPagarAndFlgActivoTrue(Integer idCuentaPagar);

    boolean existsByIdRecepcionesAndFlgActivoTrue(Integer idRecepciones);
}