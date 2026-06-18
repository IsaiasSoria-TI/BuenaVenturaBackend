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

        Usuario usuario = usuarioRepository.findByUsuarioIgnoreCaseAndFlgActivoTrue(username)
                .orElseThrow(() -> new RuntimeException("Credenciales invalidas"));

        if (usuario.getRol() == null || usuario.getRol().getFlgActivo() == null || !usuario.getRol().getFlgActivo()) {
            throw new RuntimeException("Usuario no autorizado");
        }

        if (usuario.getPersona() == null || usuario.getPersona().getFlgActivo() == null || !usuario.getPersona().getFlgActivo()) {
            throw new RuntimeException("Usuario no autorizado");
        }

        if (!isPasswordValid(password, usuario)) {
            throw new RuntimeException("Credenciales invalidas");
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

    private boolean isPasswordValid(String rawPassword, Usuario usuario) {
        String storedPassword = usuario.getContrasena();

        if (storedPassword == null || storedPassword.isBlank()) {
            return false;
        }

        if (isBcryptHash(storedPassword)) {
            return passwordEncoder.matches(rawPassword, storedPassword);
        }

        boolean matchesPlainTextPassword = rawPassword.equals(storedPassword);

        if (matchesPlainTextPassword) {
            usuario.setContrasena(passwordEncoder.encode(rawPassword));
            usuarioRepository.save(usuario);
        }

        return matchesPlainTextPassword;
    }

    private boolean isBcryptHash(String password) {
        return password.startsWith("$2a$")
                || password.startsWith("$2b$")
                || password.startsWith("$2y$");
    }
}
