package com.buenaventura.erp.impuesto.repository;

import com.buenaventura.erp.impuesto.entity.Impuesto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImpuestoRepository extends JpaRepository<Impuesto, Integer> {
}