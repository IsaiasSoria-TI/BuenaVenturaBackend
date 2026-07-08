package com.buenaventura.erp.configuracion.service;

import com.buenaventura.erp.configuracion.dto.PerfilResponse;
import com.buenaventura.erp.configuracion.dto.PerfilUpdateRequest;
import com.buenaventura.erp.configuracion.dto.SeguridadUsuarioRequest;
import com.buenaventura.erp.configuracion.dto.SeguridadUsuarioResponse;
import com.buenaventura.erp.persona.entity.Persona;
import com.buenaventura.erp.persona.repository.PersonaRepository;
import com.buenaventura.erp.rol.entity.Rol;
import com.buenaventura.erp.rol.repository.RolRepository;
import com.buenaventura.erp.usuario.entity.Usuario;
import com.buenaventura.erp.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ConfiguracionServiceImpl implements ConfiguracionService {

    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final boolean passwordHashingEnabled;

    public ConfiguracionServiceImpl(UsuarioRepository usuarioRepository,
                                    PersonaRepository personaRepository,
                                    RolRepository rolRepository,
                                    PasswordEncoder passwordEncoder,
                                    @Value("${app.security.password-hashing-enabled:true}") boolean passwordHashingEnabled) {
        this.usuarioRepository = usuarioRepository;
        this.personaRepository = personaRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordHashingEnabled = passwordHashingEnabled;
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
        persona.setApellidoMaterno(request.getApellidoMaterno() != null ? request.getApellidoMaterno().trim() : "");
        persona.setTelefono(request.getTelefono() != null ? request.getTelefono().trim() : null);
        persona.setCorreo(request.getCorreo() != null ? request.getCorreo().trim() : null);

        usuarioActual.setUsuario(nuevoUsuario);
        personaRepository.save(persona);
        usuarioRepository.save(usuarioActual);

        return toPerfilResponse(usuarioActual);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeguridadUsuarioResponse> listarUsuariosSeguridad() {
        return usuarioRepository.findAllByOrderByUsuarioAsc()
                .stream()
                .filter(usuario -> usuario.getPersona() != null)
                .map(this::toSeguridadUsuarioResponse)
                .toList();
    }

    @Override
    public SeguridadUsuarioResponse crearUsuarioSeguridad(SeguridadUsuarioRequest request) {
        String nuevoUsuario = cleanRequired(request.getUsuario());

        usuarioRepository.findByUsuarioIgnoreCase(nuevoUsuario)
                .ifPresent(usuario -> {
                    throw new RuntimeException("El nombre de usuario ya esta en uso");
                });

        if (request.getContrasena() == null || request.getContrasena().trim().isEmpty()) {
            throw new RuntimeException("La contrasena es obligatoria");
        }

        Rol rol = rolRepository.findFirstByFlgActivoTrueOrderByIdRolAsc()
                .orElseThrow(() -> new RuntimeException("No existe un rol activo para asignar"));

        Persona persona = new Persona();
        applyPersonaData(persona, request);
        persona.setFlgActivo(true);

        Usuario usuario = new Usuario();
        usuario.setRol(rol);
        usuario.setPersona(persona);
        usuario.setUsuario(nuevoUsuario);
        usuario.setContrasena(preparePasswordForStorage(request.getContrasena().trim()));
        usuario.setFlgActivo(true);

        personaRepository.save(persona);
        usuarioRepository.save(usuario);

        return toSeguridadUsuarioResponse(usuario);
    }

    @Override
    public SeguridadUsuarioResponse actualizarUsuarioSeguridad(Integer idUsuario, SeguridadUsuarioRequest request, String currentUsername) {
        Usuario usuarioActual = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String nuevoUsuario = cleanRequired(request.getUsuario());

        usuarioRepository.findByUsuarioIgnoreCase(nuevoUsuario)
                .ifPresent(existente -> {
                    if (!existente.getIdUsuario().equals(usuarioActual.getIdUsuario())) {
                        throw new RuntimeException("El nombre de usuario ya esta en uso");
                    }
                });

        Persona persona = usuarioActual.getPersona();

        if (persona == null) {
            throw new RuntimeException("El usuario no tiene persona asociada");
        }

        applyPersonaData(persona, request);
        usuarioActual.setUsuario(nuevoUsuario);

        if (request.getContrasena() != null && !request.getContrasena().trim().isEmpty()) {
            usuarioActual.setContrasena(preparePasswordForStorage(request.getContrasena().trim()));
        }

        boolean activo = request.getFlgActivo() == null || request.getFlgActivo();

        if (!activo && usuarioActual.getUsuario().equalsIgnoreCase(currentUsername)) {
            throw new RuntimeException("No puedes inactivar tu propio usuario");
        }

        usuarioActual.setFlgActivo(activo);
        persona.setFlgActivo(activo);

        personaRepository.save(persona);
        usuarioRepository.save(usuarioActual);

        return toSeguridadUsuarioResponse(usuarioActual);
    }

    @Override
    public void inactivarUsuarioSeguridad(Integer idUsuario, String currentUsername) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuario.getUsuario().equalsIgnoreCase(currentUsername)) {
            throw new RuntimeException("No puedes inactivar tu propio usuario");
        }

        usuario.setFlgActivo(false);

        if (usuario.getPersona() != null) {
            usuario.getPersona().setFlgActivo(false);
            personaRepository.save(usuario.getPersona());
        }

        usuarioRepository.save(usuario);
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

    private SeguridadUsuarioResponse toSeguridadUsuarioResponse(Usuario usuario) {
        Persona persona = usuario.getPersona();
        String nombreCompleto = formatNombreCompleto(persona);

        return new SeguridadUsuarioResponse(
                usuario.getIdUsuario(),
                persona != null ? persona.getIdPersona() : null,
                usuario.getUsuario(),
                nombreCompleto,
                getContrasenaSegura(usuario.getContrasena()),
                persona != null ? persona.getNombres() : "",
                persona != null ? persona.getApellidoPaterno() : "",
                persona != null ? persona.getApellidoMaterno() : "",
                persona != null ? persona.getTelefono() : "",
                persona != null ? persona.getDni() : "",
                persona != null ? persona.getCorreo() : "",
                usuario.getFlgActivo()
        );
    }

    private String getContrasenaSegura(String contrasena) {
        if (contrasena == null || contrasena.isBlank()) {
            return "";
        }

        if (contrasena.startsWith("$2a$") || contrasena.startsWith("$2b$") || contrasena.startsWith("$2y$")) {
            return "Contrasena protegida - restablecer";
        }

        if (!passwordHashingEnabled) {
            return contrasena;
        }

        return "Pendiente de migracion";
    }

    private String preparePasswordForStorage(String rawPassword) {
        if (!passwordHashingEnabled) {
            return rawPassword;
        }

        return passwordEncoder.encode(rawPassword);
    }

    private String formatNombreCompleto(Persona persona) {
        if (persona == null) {
            return "";
        }

        return String.join(" ",
                persona.getNombres() != null ? persona.getNombres().trim() : "",
                persona.getApellidoPaterno() != null ? persona.getApellidoPaterno().trim() : "",
                persona.getApellidoMaterno() != null ? persona.getApellidoMaterno().trim() : ""
        ).trim().replaceAll("\\s+", " ");
    }

    private void applyPersonaData(Persona persona, SeguridadUsuarioRequest request) {
        persona.setNombres(cleanRequired(request.getNombres()));
        persona.setApellidoPaterno(cleanRequired(request.getApellidoPaterno()));
        persona.setApellidoMaterno(cleanOptional(request.getApellidoMaterno()));
        persona.setTelefono(cleanOptional(request.getTelefono()));
        persona.setDni(cleanOptional(request.getDni()));
        persona.setCorreo(cleanOptional(request.getCorreo()));
    }

    private String cleanRequired(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException("Complete los campos obligatorios");
        }

        return value.trim();
    }

    private String cleanOptional(String value) {
        return value != null ? value.trim() : "";
    }
}
