package com.buenaventura.erp.tipocambio.service;

import com.buenaventura.erp.tipocambio.dto.TipoCambioRequest;
import com.buenaventura.erp.tipocambio.dto.TipoCambioResponse;
import com.buenaventura.erp.tipocambio.entity.TipoCambio;
import com.buenaventura.erp.tipocambio.repository.TipoCambioRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TipoCambioServiceImpl implements TipoCambioService {

    private final TipoCambioRepository tipoCambioRepository;

    public TipoCambioServiceImpl(TipoCambioRepository tipoCambioRepository) {
        this.tipoCambioRepository = tipoCambioRepository;
    }

    @Override
    public List<TipoCambioResponse> listar() {
        return tipoCambioRepository.findByFlgActivoTrueOrderByFechaDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<TipoCambioResponse> listarTodos() {
        return tipoCambioRepository.findAllByOrderByFechaDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public TipoCambioResponse crear(TipoCambioRequest request) {
        LocalDate fecha = validarFecha(request.getFecha());

        tipoCambioRepository.findByFecha(fecha)
                .ifPresent(tipoCambio -> {
                    throw new RuntimeException("Ya existe un tipo de cambio para esa fecha");
                });

        TipoCambio tipoCambio = new TipoCambio();
        tipoCambio.setFecha(fecha);
        tipoCambio.setValor(normalizarValor(request.getValor()));
        tipoCambio.setFlgActivo(request.getFlgActivo() == null ? true : request.getFlgActivo());
        tipoCambio.setFechaActualizacion(LocalDateTime.now());

        return toResponse(tipoCambioRepository.save(tipoCambio));
    }

    @Override
    public TipoCambioResponse actualizar(Integer id, TipoCambioRequest request) {
        TipoCambio tipoCambio = tipoCambioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de cambio no encontrado"));

        LocalDate fecha = validarFecha(request.getFecha());

        tipoCambioRepository.findByFecha(fecha)
                .filter(existing -> !existing.getIdTipoCambio().equals(id))
                .ifPresent(existing -> {
                    throw new RuntimeException("Ya existe un tipo de cambio para esa fecha");
                });

        tipoCambio.setFecha(fecha);
        tipoCambio.setValor(normalizarValor(request.getValor()));
        if (request.getFlgActivo() != null) {
            tipoCambio.setFlgActivo(request.getFlgActivo());
        }
        tipoCambio.setFechaActualizacion(LocalDateTime.now());

        return toResponse(tipoCambioRepository.save(tipoCambio));
    }

    @Override
    public void eliminar(Integer id) {
        TipoCambio tipoCambio = tipoCambioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de cambio no encontrado"));

        tipoCambio.setFlgActivo(false);
        tipoCambio.setFechaActualizacion(LocalDateTime.now());
        tipoCambioRepository.save(tipoCambio);
    }

    @Override
    public TipoCambioResponse buscarAplicable(LocalDate fecha) {
        TipoCambio tipoCambio = tipoCambioRepository
                .findFirstByFechaLessThanEqualAndFlgActivoTrueOrderByFechaDesc(validarFecha(fecha))
                .orElseThrow(() -> new RuntimeException("No existe tipo de cambio registrado para la fecha seleccionada."));

        return toResponse(tipoCambio);
    }

    private LocalDate validarFecha(LocalDate fecha) {
        if (fecha == null) {
            throw new RuntimeException("La fecha es obligatoria");
        }
        return fecha;
    }

    private BigDecimal normalizarValor(BigDecimal valor) {
        if (valor == null) {
            throw new RuntimeException("El valor es obligatorio");
        }

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El valor debe ser mayor a 0");
        }

        return valor.setScale(4, RoundingMode.HALF_UP);
    }

    private TipoCambioResponse toResponse(TipoCambio tipoCambio) {
        TipoCambioResponse response = new TipoCambioResponse();
        response.setIdTipoCambio(tipoCambio.getIdTipoCambio());
        response.setFecha(tipoCambio.getFecha());
        response.setValor(tipoCambio.getValor());
        response.setFlgActivo(Boolean.TRUE.equals(tipoCambio.getFlgActivo()));
        response.setFechaCreacion(tipoCambio.getFechaCreacion());
        response.setFechaActualizacion(tipoCambio.getFechaActualizacion());
        return response;
    }
}
