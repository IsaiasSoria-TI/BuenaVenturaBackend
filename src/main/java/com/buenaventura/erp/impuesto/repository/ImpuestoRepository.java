package com.buenaventura.erp.impuesto.repository;

import com.buenaventura.erp.impuesto.entity.Impuesto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImpuestoRepository extends JpaRepository<Impuesto, Integer> {
    List<Impuesto> findByFlgActivoTrueOrderByTipoImpuestoAsc();

    List<Impuesto> findAllByOrderByTipoImpuestoAsc();

    Optional<Impuesto> findByTipoImpuestoIgnoreCase(String tipoImpuesto);
}
