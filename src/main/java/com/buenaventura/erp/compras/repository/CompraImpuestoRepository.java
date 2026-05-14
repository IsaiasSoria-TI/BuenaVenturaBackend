package com.buenaventura.erp.compras.repository;

import com.buenaventura.erp.compras.entity.CompraImpuesto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompraImpuestoRepository extends JpaRepository<CompraImpuesto, Integer> {

    @EntityGraph(attributePaths = {"impuesto"})
    List<CompraImpuesto> findByCompra_IdComprasAndFlgActivoTrueOrderByIdCompraImpuestoAsc(Integer idCompras);

    @Query("""
            select compraImpuesto
            from CompraImpuesto compraImpuesto
            join fetch compraImpuesto.impuesto
            join fetch compraImpuesto.compra compra
            where compra.idCompras in :idsCompras
              and compraImpuesto.flgActivo = true
            order by compra.idCompras asc, compraImpuesto.idCompraImpuesto asc
            """)
    List<CompraImpuesto> findActivosByCompraIds(
            @Param("idsCompras") List<Integer> idsCompras
    );
}
