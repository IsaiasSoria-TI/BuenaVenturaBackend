package com.buenaventura.erp.recepciones.repository;

import com.buenaventura.erp.recepciones.entity.Recepcion;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecepcionRepository extends JpaRepository<Recepcion, Integer> {

    @EntityGraph(attributePaths = {"compra", "compra.proveedor"})
    List<Recepcion> findByFlgActivoTrueOrderByFechaRecepcionDesc();

    @EntityGraph(attributePaths = {"compra", "compra.proveedor"})
    List<Recepcion> findByCompra_IdComprasAndFlgActivoTrueOrderByFechaRecepcionAsc(Integer idCompras);

    List<Recepcion> findByIdRecepcionesIn(List<Integer> idsRecepciones);

    @EntityGraph(attributePaths = {"compra", "compra.proveedor"})
    @Query("""
            SELECT r
            FROM Recepcion r
            WHERE r.compra.idCompras = :idCompras
              AND r.flgActivo = true
            ORDER BY r.fechaRecepcion ASC
            """)
    List<Recepcion> obtenerPorCompra(@Param("idCompras") Integer idCompras);
}
