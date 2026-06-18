package com.buenaventura.erp.compras.repository;

import com.buenaventura.erp.compras.entity.CompraDetalle;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompraDetalleRepository extends JpaRepository<CompraDetalle, Integer> {

    @EntityGraph(attributePaths = {"articulo", "articulo.categoria", "compra"})
    List<CompraDetalle> findByCompra_IdComprasAndFlgActivoTrueOrderByIdCompraDetalleAsc(Integer idCompras);

    @Query("""
            select detalle
            from CompraDetalle detalle
            join fetch detalle.articulo articulo
            join fetch articulo.categoria
            join fetch detalle.compra compra
            where compra.idCompras in :idsCompras
              and detalle.flgActivo = true
            order by compra.idCompras asc, detalle.idCompraDetalle asc
            """)
    List<CompraDetalle> findActivosByCompraIds(
            @Param("idsCompras") List<Integer> idsCompras
    );
}
