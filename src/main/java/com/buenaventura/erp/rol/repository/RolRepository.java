package com.buenaventura.erp.rol.repository;

import com.buenaventura.erp.rol.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Integer> {

    Optional<Rol> findFirstByFlgActivoTrueOrderByIdRolAsc();
}
