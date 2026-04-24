package com.buenaventura.erp.proveedores.service;

import com.buenaventura.erp.proveedores.banco.entity.Banco;
import com.buenaventura.erp.proveedores.banco.entity.BancoProveedor;
import com.buenaventura.erp.proveedores.banco.repository.BancoProveedorRepository;
import com.buenaventura.erp.proveedores.banco.repository.BancoRepository;
import com.buenaventura.erp.proveedores.dto.ProveedorRequest;
import com.buenaventura.erp.proveedores.dto.ProveedorResponse;
import com.buenaventura.erp.proveedores.entity.Proveedor;
import com.buenaventura.erp.proveedores.repository.ProveedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final BancoProveedorRepository bancoProveedorRepository;
    private final BancoRepository bancoRepository;

    public ProveedorServiceImpl(
            ProveedorRepository proveedorRepository,
            BancoProveedorRepository bancoProveedorRepository,
            BancoRepository bancoRepository
    ) {
        this.proveedorRepository = proveedorRepository;
        this.bancoProveedorRepository = bancoProveedorRepository;
        this.bancoRepository = bancoRepository;
    }

    @Override
    public List<ProveedorResponse> listar() {
        return proveedorRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
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
        proveedor.setFechaActualizacion(LocalDateTime.now());

        Proveedor guardado = proveedorRepository.save(proveedor);

        guardarBancoProveedor(guardado.getIdProveedor(), request);

        return toResponse(guardado);
    }

    @Override
    @Transactional
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

        guardarBancoProveedor(actualizado.getIdProveedor(), request);

        return toResponse(actualizado);
    }

    @Override
    @Transactional
    public void eliminarLogico(Integer id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        proveedor.setFlgActivo(false);
        proveedor.setFechaActualizacion(LocalDateTime.now());
        proveedorRepository.save(proveedor);

        bancoProveedorRepository.findByIdProveedorAndFlgActivo(id, true)
                .ifPresent(bp -> {
                    bp.setFlgActivo(false);
                    bancoProveedorRepository.save(bp);
                });
    }

    private void guardarBancoProveedor(Integer idProveedor, ProveedorRequest request) {
        if (request.getIdBanco() == null) return;

        Banco banco = bancoRepository.findById(request.getIdBanco())
                .orElseThrow(() -> new RuntimeException("Banco no encontrado con id: " + request.getIdBanco()));

        BancoProveedor bancoProveedor = bancoProveedorRepository
                .findByIdProveedorAndFlgActivo(idProveedor, true)
                .orElseGet(BancoProveedor::new);

        bancoProveedor.setIdProveedor(idProveedor);
        bancoProveedor.setIdBanco(banco.getIdBanco());
        bancoProveedor.setCuentaBancaria(request.getCuentaBancaria());
        bancoProveedor.setCuentaInterbancaria(request.getCuentaInterbancaria());
        bancoProveedor.setFlgActivo(true);

        bancoProveedorRepository.save(bancoProveedor);
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

        bancoProveedorRepository.findByIdProveedorAndFlgActivo(proveedor.getIdProveedor(), true)
                .ifPresent(bp -> {
                    response.setIdBanco(bp.getIdBanco());
                    response.setCuentaBancaria(bp.getCuentaBancaria());
                    response.setCuentaInterbancaria(bp.getCuentaInterbancaria());

                    Banco banco = bancoRepository.findById(bp.getIdBanco()).orElse(null);
                    if (banco != null) {
                        response.setNombreBanco(banco.getNombre());
                    }
                });

        return response;
    }
}