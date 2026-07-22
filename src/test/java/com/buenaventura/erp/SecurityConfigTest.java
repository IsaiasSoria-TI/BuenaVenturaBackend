package com.buenaventura.erp;

import com.buenaventura.erp.auth.controller.AuthController;
import com.buenaventura.erp.auth.dto.LoginResponse;
import com.buenaventura.erp.auth.service.AuthService;
import com.buenaventura.erp.config.SecurityConfig;
import com.buenaventura.erp.security.CustomUserDetailsService;
import com.buenaventura.erp.security.JwtAuthenticationFilter;
import com.buenaventura.erp.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void permiteLoginSinToken() throws Exception {
        when(authService.login(any())).thenReturn(new LoginResponse(
                "token", 1, "admin", "ADMIN", "Admin", "Sistema", "", "Admin Sistema"
        ));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuario\":\"admin\",\"contrasena\":\"secreto\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));
    }

    @Test
    void rechazaEndpointPrivadoSinToken() throws Exception {
        mockMvc.perform(get("/api/recurso-inexistente"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void filtroJwtAutenticaAntesDeLlegarAlControlador() throws Exception {
        when(jwtService.extractUsername("token-valido")).thenReturn("admin");
        when(jwtService.isTokenValid("token-valido", "admin")).thenReturn(true);
        when(customUserDetailsService.loadUserByUsername("admin"))
                .thenReturn(new User("admin", "", List.of()));

        mockMvc.perform(get("/api/recurso-inexistente")
                        .header("Authorization", "Bearer token-valido"))
                .andExpect(status().isNotFound());
    }
}
