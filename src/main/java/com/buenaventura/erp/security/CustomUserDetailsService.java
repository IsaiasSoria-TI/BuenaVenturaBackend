package com.buenaventura.erp.security;

import com.buenaventura.erp.usuario.entity.Usuario;
import com.buenaventura.erp.usuario.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsuarioIgnoreCaseAndFlgActivoTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        String rol = usuario.getRol().getRol();

        return new User(
                usuario.getUsuario(),
                usuario.getContrasena(),
                usuario.getFlgActivo() != null && usuario.getFlgActivo(),
                true,
                true,
                true,
                List.of(new SimpleGrantedAuthority("ROLE_" + rol))
        );
    }
}