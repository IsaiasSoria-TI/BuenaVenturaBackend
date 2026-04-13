package com.buenaventura.erp.compras.repository;

import com.buenaventura.erp.compras.entity.Compra;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompraRepository extends JpaRepository<Compra, Integer> {

    @EntityGraph(attributePaths = {"impuesto", "pago", "proveedor", "articulo"})
    List<Compra> findByFlgActivoTrueOrderByFechaComprasDesc();
}