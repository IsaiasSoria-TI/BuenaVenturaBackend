package com.buenaventura.erp.compras.repository;

import com.buenaventura.erp.compras.entity.CompraDetalle;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompraDetalleRepository extends JpaRepository<CompraDetalle, Integer> {

    @EntityGraph(attributePaths = {"articulo", "compra"})
    List<CompraDetalle> findByCompra_IdComprasAndFlgActivoTrueOrderByIdCompraDetalleAsc(Integer idCompras);
}