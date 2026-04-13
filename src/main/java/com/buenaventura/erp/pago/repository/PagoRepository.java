package com.buenaventura.erp.pago.repository;

import com.buenaventura.erp.pago.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagoRepository extends JpaRepository<Pago, Integer> {
}