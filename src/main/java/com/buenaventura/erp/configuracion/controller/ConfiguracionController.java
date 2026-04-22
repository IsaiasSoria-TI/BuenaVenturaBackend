package com.buenaventura.erp.configuracion.controller;

import com.buenaventura.erp.configuracion.dto.PerfilResponse;
import com.buenaventura.erp.configuracion.dto.PerfilUpdateRequest;
import com.buenaventura.erp.configuracion.service.ConfiguracionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/configuracion")
public class ConfiguracionController {

    private final ConfiguracionService configuracionService;

    public ConfiguracionController(ConfiguracionService configuracionService) {
        this.configuracionService = configuracionService;
    }

    @GetMapping("/perfil")
    public ResponseEntity<PerfilResponse> obtenerPerfil(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(configuracionService.obtenerPerfil(username));
    }

    @PutMapping("/perfil")
    public ResponseEntity<PerfilResponse> actualizarPerfil(
            Authentication authentication,
            @Valid @RequestBody PerfilUpdateRequest request
    ) {
        String username = authentication.getName();
        return ResponseEntity.ok(configuracionService.actualizarPerfil(username, request));
    }
}