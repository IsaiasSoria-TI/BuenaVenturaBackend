package com.buenaventura.erp.moneda.repository;

import com.buenaventura.erp.moneda.entity.Moneda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MonedaRepository extends JpaRepository<Moneda, Integer> {
    List<Moneda> findAllByOrderByCodigoAsc();

    Optional<Moneda> findByCodigoIgnoreCase(String codigo);
}
