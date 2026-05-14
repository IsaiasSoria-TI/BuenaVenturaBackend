package com.buenaventura.erp.moneda.service;

import com.buenaventura.erp.moneda.dto.MonedaResponse;
import com.buenaventura.erp.moneda.entity.Moneda;
import com.buenaventura.erp.moneda.repository.MonedaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonedaServiceImpl implements MonedaService {

    private final MonedaRepository monedaRepository;

    public MonedaServiceImpl(MonedaRepository monedaRepository) {
        this.monedaRepository = monedaRepository;
    }

    @Override
    public List<MonedaResponse> listar() {
        return monedaRepository.findAllByOrderByCodigoAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private MonedaResponse toResponse(Moneda moneda) {
        MonedaResponse response = new MonedaResponse();
        response.setIdMoneda(moneda.getIdMoneda());
        response.setCodigo(moneda.getCodigo());
        response.setNombre(moneda.getNombre());
        response.setSimbolo(moneda.getSimbolo());
        return response;
    }
}
