package com.buenaventura.erp.proveedores.banco.repository;

import com.buenaventura.erp.proveedores.banco.entity.Banco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BancoRepository extends JpaRepository<Banco, Integer> {
}