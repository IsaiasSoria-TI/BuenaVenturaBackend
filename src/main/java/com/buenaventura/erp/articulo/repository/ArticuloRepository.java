package com.buenaventura.erp.articulo.repository;

import com.buenaventura.erp.articulo.entity.Articulo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticuloRepository extends JpaRepository<Articulo, Integer> {

    @Override
    @EntityGraph(attributePaths = {"categoria", "tipoEnvase"})
    List<Articulo> findAll();
}