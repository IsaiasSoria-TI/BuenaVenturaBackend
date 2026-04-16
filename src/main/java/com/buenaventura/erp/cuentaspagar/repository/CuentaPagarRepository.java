package com.buenaventura.erp.cuentaspagar.repository;

import com.buenaventura.erp.cuentaspagar.entity.CuentaPagar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CuentaPagarRepository extends JpaRepository<CuentaPagar, Integer> {

    List<CuentaPagar> findByFlgActivoTrueAndEstadoOrderByFechaCreacionDesc(String estado);

    boolean existsByIdRecepcionesAndFlgActivoTrue(Integer idRecepciones);
}