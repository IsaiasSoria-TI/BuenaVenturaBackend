package com.buenaventura.erp.articulo.repository;

import com.buenaventura.erp.articulo.entity.Articulo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticuloRepository extends JpaRepository<Articulo, Integer> {
}