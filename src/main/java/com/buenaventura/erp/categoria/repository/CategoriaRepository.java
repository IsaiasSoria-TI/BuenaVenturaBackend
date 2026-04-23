package com.buenaventura.erp.categoria.repository;

import com.buenaventura.erp.categoria.entity.Categoria;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    @Override
    @EntityGraph(attributePaths = {"cuentaContable"})
    List<Categoria> findAll();

    @EntityGraph(attributePaths = {"cuentaContable"})
    List<Categoria> findByEstado(String estado);
}