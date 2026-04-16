package com.buenaventura.erp.recepciones.repository;

import com.buenaventura.erp.recepciones.entity.Recepcion;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface RecepcionRepository extends JpaRepository<Recepcion, Integer> {

    @EntityGraph(attributePaths = {"compra", "compra.proveedor", "compra.articulo"})
    List<Recepcion> findAllByOrderByFechaRecepcionDesc();

    @Query("""
            select coalesce(sum(r.recibido), 0)
            from Recepcion r
            where r.compra.idCompras = :idCompras
            """)
    BigDecimal sumarRecibidoPorCompra(@Param("idCompras") Integer idCompras);

    @EntityGraph(attributePaths = {
            "compra",
            "compra.proveedor",
            "compra.articulo",
            "compra.impuesto",
            "compra.pago"
    })
    @Query("""
            select r
            from Recepcion r
            where r.compra.idCompras = :idCompras
            order by r.fechaRecepcion desc
            """)
    List<Recepcion> obtenerPorCompra(@Param("idCompras") Integer idCompras);
}