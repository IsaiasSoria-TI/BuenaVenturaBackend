package com.buenaventura.erp;

import com.buenaventura.erp.auth.dto.LoginRequest;
import com.buenaventura.erp.auth.dto.LoginResponse;
import com.buenaventura.erp.auth.service.AuthServiceImpl;
import com.buenaventura.erp.common.exception.UnauthorizedException;
import com.buenaventura.erp.persona.entity.Persona;
import com.buenaventura.erp.rol.entity.Rol;
import com.buenaventura.erp.security.JwtService;
import com.buenaventura.erp.usuario.entity.Usuario;
import com.buenaventura.erp.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(usuarioRepository, passwordEncoder, jwtService, false);
    }

    @Test
    void conservaEspaciosComoParteDeLaContrasena() {
        Usuario usuario = usuarioActivo(" clave segura ");
        when(usuarioRepository.findByUsuarioIgnoreCaseAndFlgActivoTrue("admin"))
                .thenReturn(Optional.of(usuario));
        when(jwtService.generateToken("admin", "ADMIN")).thenReturn("token");

        LoginResponse response = authService.login(loginRequest(" admin ", " clave segura "));

        assertEquals("token", response.getToken());
    }

    @Test
    void noAceptaUnaVersionRecortadaDeLaContrasena() {
        Usuario usuario = usuarioActivo(" clave segura ");
        when(usuarioRepository.findByUsuarioIgnoreCaseAndFlgActivoTrue("admin"))
                .thenReturn(Optional.of(usuario));

        assertThrows(
                UnauthorizedException.class,
                () -> authService.login(loginRequest("admin", "clave segura"))
        );
    }

    private LoginRequest loginRequest(String username, String password) {
        LoginRequest request = new LoginRequest();
        request.setUsuario(username);
        request.setContrasena(password);
        return request;
    }

    private Usuario usuarioActivo(String password) {
        Rol rol = new Rol();
        rol.setRol("ADMIN");
        rol.setFlgActivo(true);

        Persona persona = new Persona();
        persona.setNombres("Admin");
        persona.setApellidoPaterno("Sistema");
        persona.setApellidoMaterno("");
        persona.setFlgActivo(true);

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setUsuario("admin");
        usuario.setContrasena(password);
        usuario.setRol(rol);
        usuario.setPersona(persona);
        usuario.setFlgActivo(true);
        return usuario;
    }
}
