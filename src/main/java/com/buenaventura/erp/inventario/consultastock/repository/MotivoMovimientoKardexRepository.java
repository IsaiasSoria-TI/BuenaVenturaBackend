package com.buenaventura.erp.inventario.consultastock.repository;

import com.buenaventura.erp.inventario.consultastock.entity.MotivoMovimientoKardex;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MotivoMovimientoKardexRepository extends JpaRepository<MotivoMovimientoKardex, Integer> {

    List<MotivoMovimientoKardex> findByFlgActivoTrueOrderByIdMotivoMovimientoAsc();
}
