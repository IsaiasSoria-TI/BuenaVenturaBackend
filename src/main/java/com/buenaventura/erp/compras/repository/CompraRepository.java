package com.buenaventura.erp.compras.repository;

import com.buenaventura.erp.compras.entity.Compra;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompraRepository extends JpaRepository<Compra, Integer> {

    @EntityGraph(attributePaths = {"impuesto", "pago", "proveedor", "articulo", "moneda"})
    List<Compra> findByFlgActivoTrueOrderByFechaComprasDesc();

    @Query("""
            select compra
            from Compra compra
            left join fetch compra.impuesto
            left join fetch compra.pago
            left join fetch compra.proveedor
            left join fetch compra.articulo
            left join fetch compra.moneda
            order by compra.fechaCompras desc
            """)
    List<Compra> findTodasParaListado();
}
