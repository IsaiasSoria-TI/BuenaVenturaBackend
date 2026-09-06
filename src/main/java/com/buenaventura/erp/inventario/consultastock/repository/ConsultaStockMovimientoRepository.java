package com.buenaventura.erp.inventario.consultastock.repository;

import com.buenaventura.erp.inventario.consultastock.entity.ConsultaStockMovimiento;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConsultaStockMovimientoRepository extends JpaRepository<ConsultaStockMovimiento, Long> {

    @EntityGraph(attributePaths = {"articulo"})
    List<ConsultaStockMovimiento> findByPeriodoAndArticulo_IdArticuloAndFlgActivoTrueOrderByFechaMovimientoAscIdConsultaStockMovimientoAsc(
            String periodo,
            Integer idArticulo
    );

    List<ConsultaStockMovimiento> findByReferenciaTipoAndReferenciaIdAndFlgActivoTrue(
            String referenciaTipo,
            Long referenciaId
    );

    @Query("""
            SELECT m
            FROM ConsultaStockMovimiento m
            JOIN FETCH m.articulo a
            WHERE m.flgActivo = true
              AND (:periodo IS NULL OR :periodo = '' OR m.periodo = :periodo)
              AND (:idArticulo IS NULL OR a.idArticulo = :idArticulo)
              AND (
                    :busqueda IS NULL
                    OR :busqueda = ''
                    OR LOWER(a.descripcion) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                    OR CAST(a.idArticulo AS string) LIKE CONCAT('%', :busquedaNumerica, '%')
                    OR LOWER(m.documento) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                    OR LOWER(m.codigoMovimiento) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                    OR LOWER(m.tipoMovimiento) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                    OR (:buscarIngreso = true AND UPPER(m.tipoMovimiento) IN ('INGRESO', 'ENTRADA', 'COMPRA'))
                    OR (:buscarSalida = true AND UPPER(m.tipoMovimiento) IN ('SALIDA', 'VENTA'))
                    OR LOWER(m.proveedorMotivo) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                    OR LOWER(m.detalle) LIKE LOWER(CONCAT('%', :busqueda, '%'))
              )
            ORDER BY m.fechaMovimiento DESC, m.idConsultaStockMovimiento DESC
            """)
    List<ConsultaStockMovimiento> buscarHistorial(
            @Param("periodo") String periodo,
            @Param("idArticulo") Integer idArticulo,
            @Param("busqueda") String busqueda,
            @Param("busquedaNumerica") String busquedaNumerica,
            @Param("buscarIngreso") boolean buscarIngreso,
            @Param("buscarSalida") boolean buscarSalida
    );
}
