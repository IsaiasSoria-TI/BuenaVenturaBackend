package com.buenaventura.erp.cuentacontable.service;

import com.buenaventura.erp.cuentacontable.dto.CuentaContableRequest;
import com.buenaventura.erp.cuentacontable.dto.CuentaContableResponse;
import com.buenaventura.erp.cuentacontable.entity.CuentaContable;
import com.buenaventura.erp.cuentacontable.repository.CuentaContableRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CuentaContableServiceImpl implements CuentaContableService {

    private final CuentaContableRepository repository;

    public CuentaContableServiceImpl(CuentaContableRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CuentaContableResponse> listar() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CuentaContableResponse crear(CuentaContableRequest request) {
        CuentaContable cuenta = new CuentaContable();
        cuenta.setCodigo(request.getCodigo());
        cuenta.setDescripcion(request.getDescripcion());
        cuenta.setEstado(
                request.getEstado() == null || request.getEstado().isBlank()
                        ? "Activo"
                        : request.getEstado()
        );

        return toResponse(repository.save(cuenta));
    }

    @Override
    public CuentaContableResponse actualizar(Integer id, CuentaContableRequest request) {
        CuentaContable cuenta = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta contable no encontrada"));

        cuenta.setCodigo(request.getCodigo());
        cuenta.setDescripcion(request.getDescripcion());
        cuenta.setEstado(
                request.getEstado() == null || request.getEstado().isBlank()
                        ? cuenta.getEstado()
                        : request.getEstado()
        );

        return toResponse(repository.save(cuenta));
    }

    @Override
    public void eliminar(Integer id) {
        CuentaContable cuenta = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta contable no encontrada"));

        cuenta.setEstado("Inactivo");
        repository.save(cuenta);
    }

    private CuentaContableResponse toResponse(CuentaContable entity) {
        CuentaContableResponse response = new CuentaContableResponse();
        response.setIdCuentaContable(entity.getIdCuentaContable());
        response.setCodigo(entity.getCodigo());
        response.setDescripcion(entity.getDescripcion());
        response.setEstado(entity.getEstado());
        return response;
    }
}