package com.buenaventura.erp.inventario.consultastock.service;

import com.buenaventura.erp.inventario.consultastock.dto.MotivoMovimientoKardexResponse;
import com.buenaventura.erp.inventario.consultastock.entity.MotivoMovimientoKardex;
import com.buenaventura.erp.inventario.consultastock.repository.MotivoMovimientoKardexRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MotivoMovimientoKardexServiceImpl implements MotivoMovimientoKardexService {

    private final MotivoMovimientoKardexRepository motivoRepository;

    public MotivoMovimientoKardexServiceImpl(MotivoMovimientoKardexRepository motivoRepository) {
        this.motivoRepository = motivoRepository;
    }

    @Override
    public List<MotivoMovimientoKardexResponse> listarActivos() {
        return motivoRepository.findByFlgActivoTrueOrderByIdMotivoMovimientoAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private MotivoMovimientoKardexResponse toResponse(MotivoMovimientoKardex motivo) {
        MotivoMovimientoKardexResponse response = new MotivoMovimientoKardexResponse();
        response.setIdMotivoMovimiento(motivo.getIdMotivoMovimiento());
        response.setCodigo(motivo.getCodigo());
        response.setNombre(motivo.getNombre());
        response.setNaturaleza(motivo.getNaturaleza());
        return response;
    }
}
