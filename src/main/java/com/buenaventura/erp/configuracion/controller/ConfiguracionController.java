package com.buenaventura.erp.configuracion.controller;

import com.buenaventura.erp.common.exception.BadRequestException;
import com.buenaventura.erp.configuracion.dto.PerfilResponse;
import com.buenaventura.erp.configuracion.dto.PerfilUpdateRequest;
import com.buenaventura.erp.configuracion.dto.SeguridadUsuarioRequest;
import com.buenaventura.erp.configuracion.dto.SeguridadUsuarioResponse;
import com.buenaventura.erp.configuracion.service.ConfiguracionService;
import com.buenaventura.erp.security.JwtService;
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
    private final JwtService jwtService;

    public ConfiguracionController(ConfiguracionService configuracionService, JwtService jwtService) {
        this.configuracionService = configuracionService;
        this.jwtService = jwtService;
    }

    // Preferido: usuarioId por query string. Compatibilidad: si falta, usa el JWT.
    // Ejemplos: GET /api/configuracion/perfil?usuarioId=123 o Authorization: Bearer <token>
    @GetMapping("/perfil")
    public ResponseEntity<PerfilResponse> obtenerPerfil(
            @RequestParam(name = "usuarioId", required = false)
            @Positive(message = "El parametro usuarioId debe ser mayor a 0")
            Integer usuarioId,
            @RequestHeader(name = "Authorization", required = false) String authorization
    ) {
        return ResponseEntity.ok(configuracionService.obtenerPerfil(usuarioId, extractUsernameIfNeeded(usuarioId, authorization)));
    }

    // Preferido: usuarioId por query string. Compatibilidad: si falta, usa el JWT.
    // Ejemplos: PUT /api/configuracion/perfil?usuarioId=123 o Authorization: Bearer <token>
    @PutMapping("/perfil")
    public ResponseEntity<PerfilResponse> actualizarPerfil(
            @RequestParam(name = "usuarioId", required = false)
            @Positive(message = "El parametro usuarioId debe ser mayor a 0")
            Integer usuarioId,
            @RequestHeader(name = "Authorization", required = false) String authorization,
            @Valid @RequestBody PerfilUpdateRequest request
    ) {
        return ResponseEntity.ok(configuracionService.actualizarPerfil(usuarioId, extractUsernameIfNeeded(usuarioId, authorization), request));
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

    private String extractUsernameIfNeeded(Integer usuarioId, String authorization) {
        if (usuarioId != null) {
            return null;
        }

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return null;
        }

        try {
            return jwtService.extractUsername(authorization.substring(7));
        } catch (Exception ex) {
            throw new BadRequestException("Token invalido o expirado");
        }
    }
}
