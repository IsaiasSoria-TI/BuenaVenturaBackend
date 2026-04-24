package com.buenaventura.erp.compras.repository;

import com.buenaventura.erp.compras.entity.CompraImpuesto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompraImpuestoRepository extends JpaRepository<CompraImpuesto, Integer> {

    @EntityGraph(attributePaths = {"impuesto"})
    List<CompraImpuesto> findByCompra_IdComprasAndFlgActivoTrueOrderByIdCompraImpuestoAsc(Integer idCompras);
}