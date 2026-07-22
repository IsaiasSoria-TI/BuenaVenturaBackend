package com.buenaventura.erp.cuentaspagar.repository;

import com.buenaventura.erp.cuentaspagar.entity.CuentaPagarDetalle;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CuentaPagarDetalleRepository extends JpaRepository<CuentaPagarDetalle, Integer> {

    @EntityGraph(attributePaths = {
            "recepcionDetalle",
            "recepcionDetalle.compraDetalle",
            "recepcionDetalle.compraDetalle.articulo"
    })
    List<CuentaPagarDetalle> findByCuentaPagar_IdCuentaPagarAndFlgActivoTrueOrderByIdCuentaPagarDetalleAsc(
            Integer idCuentaPagar
    );

    @EntityGraph(attributePaths = {
            "cuentaPagar",
            "recepcionDetalle",
            "recepcionDetalle.compraDetalle",
            "recepcionDetalle.compraDetalle.articulo"
    })
    List<CuentaPagarDetalle> findByCuentaPagar_IdCuentaPagarInAndFlgActivoTrueOrderByIdCuentaPagarDetalleAsc(
            List<Integer> idsCuentaPagar
    );
}
