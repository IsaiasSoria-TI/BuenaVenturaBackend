package com.buenaventura.erp.impuesto.service;

import com.buenaventura.erp.impuesto.dto.ImpuestoRequest;
import com.buenaventura.erp.impuesto.dto.ImpuestoResponse;
import com.buenaventura.erp.impuesto.entity.Impuesto;
import com.buenaventura.erp.impuesto.repository.ImpuestoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ImpuestoServiceImpl implements ImpuestoService {

    private final ImpuestoRepository impuestoRepository;

    public ImpuestoServiceImpl(ImpuestoRepository impuestoRepository) {
        this.impuestoRepository = impuestoRepository;
    }

    @Override
    public List<ImpuestoResponse> listar() {
        return impuestoRepository.findByFlgActivoTrueOrderByTipoImpuestoAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<ImpuestoResponse> listarTodos() {
        return impuestoRepository.findAllByOrderByTipoImpuestoAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ImpuestoResponse crear(ImpuestoRequest request) {
        String tipoImpuesto = normalizarTipoImpuesto(request.getTipoImpuesto());

        impuestoRepository.findByTipoImpuestoIgnoreCase(tipoImpuesto)
                .ifPresent(impuesto -> {
                    throw new RuntimeException("Ya existe un impuesto con ese tipo");
                });

        Impuesto impuesto = new Impuesto();
        impuesto.setTipoImpuesto(tipoImpuesto);
        impuesto.setValor(normalizarValor(request.getValor()));
        impuesto.setFlgActivo(request.getFlgActivo() == null ? true : request.getFlgActivo());
        impuesto.setFechaActualizacion(LocalDateTime.now());

        return toResponse(impuestoRepository.save(impuesto));
    }

    @Override
    public ImpuestoResponse actualizar(Integer id, ImpuestoRequest request) {
        Impuesto impuesto = impuestoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Impuesto no encontrado"));

        String tipoImpuesto = normalizarTipoImpuesto(request.getTipoImpuesto());

        impuestoRepository.findByTipoImpuestoIgnoreCase(tipoImpuesto)
                .filter(existing -> !existing.getIdImpuesto().equals(id))
                .ifPresent(existing -> {
                    throw new RuntimeException("Ya existe un impuesto con ese tipo");
                });

        impuesto.setTipoImpuesto(tipoImpuesto);
        impuesto.setValor(normalizarValor(request.getValor()));
        if (request.getFlgActivo() != null) {
            impuesto.setFlgActivo(request.getFlgActivo());
        }
        impuesto.setFechaActualizacion(LocalDateTime.now());

        return toResponse(impuestoRepository.save(impuesto));
    }

    @Override
    public void eliminar(Integer id) {
        Impuesto impuesto = impuestoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Impuesto no encontrado"));

        impuesto.setFlgActivo(false);
        impuesto.setFechaActualizacion(LocalDateTime.now());
        impuestoRepository.save(impuesto);
    }

    private String normalizarTipoImpuesto(String tipoImpuesto) {
        String normalizado = tipoImpuesto == null ? "" : tipoImpuesto.trim();
        if (normalizado.isBlank()) {
            throw new RuntimeException("El tipo de impuesto es obligatorio");
        }
        return normalizado;
    }

    private BigDecimal normalizarValor(BigDecimal valor) {
        if (valor == null) {
            throw new RuntimeException("El valor del impuesto es obligatorio");
        }

        if (valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("El valor del impuesto no puede ser negativo");
        }

        return valor.setScale(2, RoundingMode.HALF_UP);
    }

    private ImpuestoResponse toResponse(Impuesto impuesto) {
        ImpuestoResponse response = new ImpuestoResponse();
        response.setIdImpuesto(impuesto.getIdImpuesto());
        response.setTipoImpuesto(impuesto.getTipoImpuesto());
        response.setValor(impuesto.getValor());
        response.setFlgActivo(Boolean.TRUE.equals(impuesto.getFlgActivo()));
        return response;
    }
}
