package com.buenaventura.erp.configuracion.service;

import com.buenaventura.erp.configuracion.dto.PerfilResponse;
import com.buenaventura.erp.configuracion.dto.PerfilUpdateRequest;

public interface ConfiguracionService {

    PerfilResponse obtenerPerfil(String username);

    PerfilResponse actualizarPerfil(String username, PerfilUpdateRequest request);
}