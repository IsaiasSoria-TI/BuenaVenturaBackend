package com.buenaventura.erp.tipocambio.repository;

import com.buenaventura.erp.tipocambio.entity.TipoCambio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TipoCambioRepository extends JpaRepository<TipoCambio, Integer> {
    List<TipoCambio> findByFlgActivoTrueOrderByFechaDesc();

    List<TipoCambio> findAllByOrderByFechaDesc();

    Optional<TipoCambio> findByFecha(LocalDate fecha);

    Optional<TipoCambio> findFirstByFechaLessThanEqualAndFlgActivoTrueOrderByFechaDesc(LocalDate fecha);
}
