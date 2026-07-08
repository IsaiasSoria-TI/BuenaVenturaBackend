package com.buenaventura.erp.configuracion.service;

import com.buenaventura.erp.configuracion.dto.PerfilResponse;
import com.buenaventura.erp.configuracion.dto.PerfilUpdateRequest;
import com.buenaventura.erp.configuracion.dto.SeguridadUsuarioRequest;
import com.buenaventura.erp.configuracion.dto.SeguridadUsuarioResponse;

import java.util.List;

public interface ConfiguracionService {

    PerfilResponse obtenerPerfil(Integer usuarioId, String username);

    PerfilResponse actualizarPerfil(Integer usuarioId, String username, PerfilUpdateRequest request);

    List<SeguridadUsuarioResponse> listarUsuariosSeguridad();

    SeguridadUsuarioResponse crearUsuarioSeguridad(SeguridadUsuarioRequest request);

    SeguridadUsuarioResponse actualizarUsuarioSeguridad(Integer idUsuario, SeguridadUsuarioRequest request, String currentUsername);

    void inactivarUsuarioSeguridad(Integer idUsuario, String currentUsername);
}
