package com.buenaventura.erp.configuracion.service;

import com.buenaventura.erp.configuracion.dto.PerfilResponse;
import com.buenaventura.erp.configuracion.dto.PerfilUpdateRequest;
import com.buenaventura.erp.persona.entity.Persona;
import com.buenaventura.erp.persona.repository.PersonaRepository;
import com.buenaventura.erp.usuario.entity.Usuario;
import com.buenaventura.erp.usuario.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ConfiguracionServiceImpl implements ConfiguracionService {

    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;

    public ConfiguracionServiceImpl(UsuarioRepository usuarioRepository,
                                    PersonaRepository personaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.personaRepository = personaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PerfilResponse obtenerPerfil(String username) {
        Usuario usuario = usuarioRepository.findByUsuarioIgnoreCaseAndFlgActivoTrue(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return toPerfilResponse(usuario);
    }

    @Override
    public PerfilResponse actualizarPerfil(String username, PerfilUpdateRequest request) {
        Usuario usuarioActual = usuarioRepository.findByUsuarioIgnoreCaseAndFlgActivoTrue(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String nuevoUsuario = request.getUsuario().trim();

        if (!usuarioActual.getUsuario().equalsIgnoreCase(nuevoUsuario)) {
            usuarioRepository.findByUsuarioIgnoreCaseAndFlgActivoTrue(nuevoUsuario)
                    .ifPresent(existente -> {
                        if (!existente.getIdUsuario().equals(usuarioActual.getIdUsuario())) {
                            throw new RuntimeException("El nombre de usuario ya está en uso");
                        }
                    });
        }

        Persona persona = usuarioActual.getPersona();

        persona.setNombres(request.getNombres().trim());
        persona.setApellidoPaterno(request.getApellidoPaterno().trim());
        persona.setApellidoMaterno(request.getApellidoMaterno().trim());
        persona.setTelefono(request.getTelefono() != null ? request.getTelefono().trim() : null);
        persona.setCorreo(request.getCorreo() != null ? request.getCorreo().trim() : null);

        usuarioActual.setUsuario(nuevoUsuario);
        personaRepository.save(persona);
        usuarioRepository.save(usuarioActual);

        return toPerfilResponse(usuarioActual);
    }

    private PerfilResponse toPerfilResponse(Usuario usuario) {
        Persona persona = usuario.getPersona();

        PerfilResponse response = new PerfilResponse();
        response.setIdUsuario(usuario.getIdUsuario());
        response.setIdPersona(persona.getIdPersona());
        response.setUsuario(usuario.getUsuario());
        response.setNombres(persona.getNombres());
        response.setApellidoPaterno(persona.getApellidoPaterno());
        response.setApellidoMaterno(persona.getApellidoMaterno());
        response.setTelefono(persona.getTelefono());
        response.setDni(persona.getDni());
        response.setCorreo(persona.getCorreo());
        response.setNombreCompleto(persona.getNombreCompleto());

        return response;
    }
}