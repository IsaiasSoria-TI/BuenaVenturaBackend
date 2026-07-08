package com.buenaventura.erp.configuracion.controller;

import com.buenaventura.erp.configuracion.dto.PerfilResponse;
import com.buenaventura.erp.configuracion.dto.PerfilUpdateRequest;
import com.buenaventura.erp.configuracion.dto.SeguridadUsuarioRequest;
import com.buenaventura.erp.configuracion.dto.SeguridadUsuarioResponse;
import com.buenaventura.erp.configuracion.service.ConfiguracionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/configuracion")
@Validated
public class ConfiguracionController {

    private final ConfiguracionService configuracionService;

    public ConfiguracionController(ConfiguracionService configuracionService) {
        this.configuracionService = configuracionService;
    }

    // Parametro obligatorio: usuarioId se envia como query string.
    // Ejemplo: GET /api/configuracion/perfil?usuarioId=123
    @GetMapping("/perfil")
    public ResponseEntity<PerfilResponse> obtenerPerfil(
            @RequestParam(name = "usuarioId", required = true)
            @Positive(message = "El parametro usuarioId debe ser mayor a 0")
            Integer usuarioId
    ) {
        return ResponseEntity.ok(configuracionService.obtenerPerfil(usuarioId));
    }

    // Parametro obligatorio: usuarioId se envia como query string.
    // Ejemplo: PUT /api/configuracion/perfil?usuarioId=123
    @PutMapping("/perfil")
    public ResponseEntity<PerfilResponse> actualizarPerfil(
            @RequestParam(name = "usuarioId", required = true)
            @Positive(message = "El parametro usuarioId debe ser mayor a 0")
            Integer usuarioId,
            @Valid @RequestBody PerfilUpdateRequest request
    ) {
        return ResponseEntity.ok(configuracionService.actualizarPerfil(usuarioId, request));
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
        return ResponseEntity.ok(configuracionService.actualizarUsuarioSeguridad(idUsuario, request, getCurrentUsername(authentication)));
    }

    @DeleteMapping("/seguridad/usuarios/{idUsuario}")
    public ResponseEntity<Void> inactivarUsuarioSeguridad(
            Authentication authentication,
            @PathVariable Integer idUsuario
    ) {
        configuracionService.inactivarUsuarioSeguridad(idUsuario, getCurrentUsername(authentication));
        return ResponseEntity.noContent().build();
    }

    private String getCurrentUsername(Authentication authentication) {
        return authentication != null ? authentication.getName() : null;
    }
}
