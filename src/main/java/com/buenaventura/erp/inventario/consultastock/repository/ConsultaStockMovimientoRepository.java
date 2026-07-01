package com.buenaventura.erp.inventario.consultastock.repository;

import com.buenaventura.erp.inventario.consultastock.entity.ConsultaStockMovimiento;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultaStockMovimientoRepository extends JpaRepository<ConsultaStockMovimiento, Long> {

    @EntityGraph(attributePaths = {"articulo"})
    List<ConsultaStockMovimiento> findByPeriodoAndArticulo_IdArticuloAndFlgActivoTrueOrderByFechaMovimientoAscIdConsultaStockMovimientoAsc(
            String periodo,
            Integer idArticulo
    );
}
