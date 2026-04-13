package com.buenaventura.erp.auth.service;

import com.buenaventura.erp.auth.dto.LoginRequest;
import com.buenaventura.erp.auth.dto.LoginResponse;
import com.buenaventura.erp.security.JwtService;
import com.buenaventura.erp.usuario.entity.Usuario;
import com.buenaventura.erp.usuario.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UsuarioRepository usuarioRepository,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        String username = request.getUsuario().trim();
        String password = request.getContrasena().trim();

        System.out.println("Usuario recibido: [" + username + "]");
        System.out.println("Contrasena recibida: [" + password + "]");

        Usuario usuario = usuarioRepository.findByUsuarioIgnoreCaseAndFlgActivoTrue(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado o inactivo"));

        System.out.println("Usuario BD: [" + usuario.getUsuario() + "]");
        System.out.println("Contrasena BD: [" + usuario.getContrasena() + "]");

        if (usuario.getRol() == null || usuario.getRol().getFlgActivo() == null || !usuario.getRol().getFlgActivo()) {
            throw new RuntimeException("El rol del usuario está inactivo");
        }

        if (usuario.getPersona() == null || usuario.getPersona().getFlgActivo() == null || !usuario.getPersona().getFlgActivo()) {
            throw new RuntimeException("La persona asociada al usuario está inactiva");
        }

        if (!password.equals(usuario.getContrasena())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        String token = jwtService.generateToken(
                usuario.getUsuario(),
                usuario.getRol().getRol()
        );

        return new LoginResponse(
                token,
                usuario.getIdUsuario(),
                usuario.getUsuario(),
                usuario.getRol().getRol(),
                usuario.getPersona().getNombres(),
                usuario.getPersona().getApellidoPaterno(),
                usuario.getPersona().getApellidoMaterno(),
                usuario.getPersona().getNombreCompleto()
        );
    }
}