package com.buenaventura.erp.cuentaspagar.repository;

import com.buenaventura.erp.cuentaspagar.entity.CuentaPagar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuentaPagarRepository extends JpaRepository<CuentaPagar, Integer> {

    List<CuentaPagar> findByFlgActivoTrue();

    boolean existsByIdRecepcionesAndFlgActivoTrue(Integer idRecepciones);

    List<CuentaPagar> findByFlgActivoTrueOrderByFechaCreacionDesc();
}