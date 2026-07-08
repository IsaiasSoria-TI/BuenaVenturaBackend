package com.buenaventura.erp.usuario.repository;

import com.buenaventura.erp.usuario.entity.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    @EntityGraph(attributePaths = {"rol", "persona"})
    Optional<Usuario> findByUsuarioIgnoreCaseAndFlgActivoTrue(String usuario);

    @EntityGraph(attributePaths = {"rol", "persona"})
    Optional<Usuario> findByUsuarioIgnoreCase(String usuario);

    @EntityGraph(attributePaths = {"persona"})
    List<Usuario> findAllByFlgActivoTrueOrderByUsuarioAsc();

    @EntityGraph(attributePaths = {"persona"})
    List<Usuario> findAllByOrderByUsuarioAsc();
}
