package com.buenaventura.erp.proveedores.service;

import com.buenaventura.erp.proveedores.dto.ProveedorRequest;
import com.buenaventura.erp.proveedores.dto.ProveedorResponse;
import com.buenaventura.erp.proveedores.entity.Proveedor;
import com.buenaventura.erp.proveedores.repository.ProveedorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorServiceImpl(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    @Override
    public List<ProveedorResponse> listar() {
        return proveedorRepository.findByFlgActivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ProveedorResponse registrar(ProveedorRequest request) {
        if (proveedorRepository.existsByRuc(request.getRuc())) {
            throw new RuntimeException("Ya existe un proveedor con ese RUC");
        }

        Proveedor proveedor = new Proveedor();
        proveedor.setRuc(request.getRuc());
        proveedor.setRazonSocial(request.getRazonSocial());
        proveedor.setTelefono(request.getTelefono());
        proveedor.setCorreo(request.getCorreo());
        proveedor.setDireccion(request.getDireccion());
        proveedor.setRepresentante(request.getRepresentante());
        proveedor.setFlgActivo(true);

        Proveedor guardado = proveedorRepository.save(proveedor);
        return toResponse(guardado);
    }

    @Override
    public ProveedorResponse actualizar(Integer id, ProveedorRequest request) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        if (!proveedor.getRuc().equals(request.getRuc())
                && proveedorRepository.existsByRuc(request.getRuc())) {
            throw new RuntimeException("Ya existe otro proveedor con ese RUC");
        }

        proveedor.setRuc(request.getRuc());
        proveedor.setRazonSocial(request.getRazonSocial());
        proveedor.setTelefono(request.getTelefono());
        proveedor.setCorreo(request.getCorreo());
        proveedor.setDireccion(request.getDireccion());
        proveedor.setRepresentante(request.getRepresentante());
        proveedor.setFechaActualizacion(LocalDateTime.now());

        Proveedor actualizado = proveedorRepository.save(proveedor);
        return toResponse(actualizado);
    }

    @Override
    public void eliminarLogico(Integer id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        proveedor.setFlgActivo(false);
        proveedor.setFechaActualizacion(LocalDateTime.now());
        proveedorRepository.save(proveedor);
    }

    private ProveedorResponse toResponse(Proveedor proveedor) {
        ProveedorResponse response = new ProveedorResponse();
        response.setIdProveedor(proveedor.getIdProveedor());
        response.setRuc(proveedor.getRuc());
        response.setRazonSocial(proveedor.getRazonSocial());
        response.setTelefono(proveedor.getTelefono());
        response.setCorreo(proveedor.getCorreo());
        response.setDireccion(proveedor.getDireccion());
        response.setRepresentante(proveedor.getRepresentante());
        response.setFlgActivo(proveedor.getFlgActivo());
        return response;
    }
}