package com.buenaventura.erp.inventario.tipoenvase.service;

import com.buenaventura.erp.common.exception.NotFoundException;
import com.buenaventura.erp.inventario.tipoenvase.dto.TipoEnvaseRequest;
import com.buenaventura.erp.inventario.tipoenvase.dto.TipoEnvaseResponse;
import com.buenaventura.erp.inventario.tipoenvase.entity.TipoEnvase;
import com.buenaventura.erp.inventario.tipoenvase.repository.TipoEnvaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TipoEnvaseServiceImpl implements TipoEnvaseService {

    private final TipoEnvaseRepository tipoEnvaseRepository;

    public TipoEnvaseServiceImpl(TipoEnvaseRepository tipoEnvaseRepository) {
        this.tipoEnvaseRepository = tipoEnvaseRepository;
    }

    @Override
    public List<TipoEnvaseResponse> listar() {
        return tipoEnvaseRepository.findAllByOrderByNombreAsc()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TipoEnvaseResponse crear(TipoEnvaseRequest request) {
        TipoEnvase tipoEnvase = new TipoEnvase();
        tipoEnvase.setNombre(request.getNombre().trim());
        tipoEnvase.setEstado(
                request.getEstado() == null || request.getEstado().isBlank()
                        ? "Activo"
                        : request.getEstado()
        );

        return toResponse(tipoEnvaseRepository.save(tipoEnvase));
    }

    @Override
    public TipoEnvaseResponse actualizar(Integer id, TipoEnvaseRequest request) {
        TipoEnvase tipoEnvase = tipoEnvaseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tipo de envase no encontrado"));

        tipoEnvase.setNombre(request.getNombre().trim());
        tipoEnvase.setEstado(
                request.getEstado() == null || request.getEstado().isBlank()
                        ? tipoEnvase.getEstado()
                        : request.getEstado()
        );

        return toResponse(tipoEnvaseRepository.save(tipoEnvase));
    }

    @Override
    public void eliminar(Integer id) {
        TipoEnvase tipoEnvase = tipoEnvaseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tipo de envase no encontrado"));

        tipoEnvase.setEstado("Inactivo");
        tipoEnvaseRepository.save(tipoEnvase);
    }

    private TipoEnvaseResponse toResponse(TipoEnvase tipoEnvase) {
        TipoEnvaseResponse response = new TipoEnvaseResponse();
        response.setIdTipoEnvase(tipoEnvase.getIdTipoEnvase());
        response.setNombre(tipoEnvase.getNombre());
        response.setEstado(tipoEnvase.getEstado());
        return response;
    }
}
