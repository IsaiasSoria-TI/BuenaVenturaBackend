package com.buenaventura.erp.recepciones.repository;

import com.buenaventura.erp.recepciones.entity.RecepcionDetalle;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface RecepcionDetalleRepository extends JpaRepository<RecepcionDetalle, Integer> {

    @EntityGraph(attributePaths = {"compraDetalle", "compraDetalle.articulo"})
    List<RecepcionDetalle> findByRecepcion_IdRecepcionesAndFlgActivoTrueOrderByIdRecepcionDetalleAsc(
            Integer idRecepciones
    );

    @Query("""
            SELECT COALESCE(SUM(rd.recibido), 0)
            FROM RecepcionDetalle rd
            WHERE rd.flgActivo = true
              AND rd.compraDetalle.idCompraDetalle = :idCompraDetalle
            """)
    BigDecimal sumarRecibidoPorCompraDetalle(@Param("idCompraDetalle") Integer idCompraDetalle);
}