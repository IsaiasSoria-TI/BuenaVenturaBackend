package com.buenaventura.erp.configuracion.controller;

import com.buenaventura.erp.configuracion.dto.PerfilResponse;
import com.buenaventura.erp.configuracion.dto.PerfilUpdateRequest;
import com.buenaventura.erp.configuracion.dto.SeguridadUsuarioRequest;
import com.buenaventura.erp.configuracion.dto.SeguridadUsuarioResponse;
import com.buenaventura.erp.configuracion.service.ConfiguracionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/seguridad/usuarios")
    public ResponseEntity<List<SeguridadUsuarioResponse>> listarUsuariosSeguridad() {
        return ResponseEntity.ok(configuracionService.listarUsuariosSeguridad());
    }

    @PostMapping("/seguridad/usuarios")
    public ResponseEntity<SeguridadUsuarioResponse> crearUsuarioSeguridad(
            @Valid @RequestBody SeguridadUsuarioRequest request
    ) {
        return ResponseEntity.ok(configuracionService.crearUsuarioSeguridad(request));
    }

    @PutMapping("/seguridad/usuarios/{idUsuario}")
    public ResponseEntity<SeguridadUsuarioResponse> actualizarUsuarioSeguridad(
            Authentication authentication,
            @PathVariable Integer idUsuario,
            @Valid @RequestBody SeguridadUsuarioRequest request
    ) {
        return ResponseEntity.ok(configuracionService.actualizarUsuarioSeguridad(idUsuario, request, authentication.getName()));
    }

    @DeleteMapping("/seguridad/usuarios/{idUsuario}")
    public ResponseEntity<Void> inactivarUsuarioSeguridad(
            Authentication authentication,
            @PathVariable Integer idUsuario
    ) {
        configuracionService.inactivarUsuarioSeguridad(idUsuario, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
