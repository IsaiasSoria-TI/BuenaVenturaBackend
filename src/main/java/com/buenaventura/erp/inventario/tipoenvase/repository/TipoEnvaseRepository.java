package com.buenaventura.erp.inventario.tipoenvase.repository;

import com.buenaventura.erp.inventario.tipoenvase.entity.TipoEnvase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipoEnvaseRepository extends JpaRepository<TipoEnvase, Integer> {

    List<TipoEnvase> findAllByOrderByNombreAsc();

    List<TipoEnvase> findByEstado(String estado);
}
