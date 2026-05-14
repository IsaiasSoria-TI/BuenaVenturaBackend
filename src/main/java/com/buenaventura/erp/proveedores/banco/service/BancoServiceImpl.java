package com.buenaventura.erp.proveedores.banco.service;

import com.buenaventura.erp.proveedores.banco.dto.BancoRequest;
import com.buenaventura.erp.proveedores.banco.dto.BancoResponse;
import com.buenaventura.erp.proveedores.banco.entity.Banco;
import com.buenaventura.erp.proveedores.banco.repository.BancoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BancoServiceImpl implements BancoService {

    private final BancoRepository bancoRepository;

    public BancoServiceImpl(BancoRepository bancoRepository) {
        this.bancoRepository = bancoRepository;
    }

    @Override
    public List<BancoResponse> listarActivos() {
        return bancoRepository.findByFlgActivoTrueOrderByNombreAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<BancoResponse> listarTodos() {
        return bancoRepository.findAllByOrderByNombreAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public BancoResponse crear(BancoRequest request) {
        String nombre = normalizarNombre(request.getNombre());

        bancoRepository.findByNombreIgnoreCase(nombre)
                .ifPresent(banco -> {
                    throw new RuntimeException("Ya existe un banco con ese nombre");
                });

        Banco banco = new Banco();
        banco.setNombre(nombre);
        banco.setFlgActivo(request.getFlgActivo() == null ? true : request.getFlgActivo());

        return toResponse(bancoRepository.save(banco));
    }

    @Override
    public BancoResponse actualizar(Integer id, BancoRequest request) {
        Banco banco = bancoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Banco no encontrado"));

        String nombre = normalizarNombre(request.getNombre());

        bancoRepository.findByNombreIgnoreCase(nombre)
                .filter(existing -> !existing.getIdBanco().equals(id))
                .ifPresent(existing -> {
                    throw new RuntimeException("Ya existe un banco con ese nombre");
                });

        banco.setNombre(nombre);
        if (request.getFlgActivo() != null) {
            banco.setFlgActivo(request.getFlgActivo());
        }

        return toResponse(bancoRepository.save(banco));
    }

    @Override
    public void eliminar(Integer id) {
        Banco banco = bancoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Banco no encontrado"));

        banco.setFlgActivo(false);
        bancoRepository.save(banco);
    }

    private String normalizarNombre(String nombre) {
        String normalizado = nombre == null ? "" : nombre.trim();
        if (normalizado.isBlank()) {
            throw new RuntimeException("El nombre del banco es obligatorio");
        }
        return normalizado;
    }

    private BancoResponse toResponse(Banco banco) {
        return new BancoResponse(
                banco.getIdBanco(),
                banco.getNombre(),
                Boolean.TRUE.equals(banco.getFlgActivo())
        );
    }
}
