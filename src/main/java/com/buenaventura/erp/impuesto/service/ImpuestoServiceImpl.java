package com.buenaventura.erp.impuesto.service;

import com.buenaventura.erp.impuesto.dto.ImpuestoResponse;
import com.buenaventura.erp.impuesto.entity.Impuesto;
import com.buenaventura.erp.impuesto.repository.ImpuestoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImpuestoServiceImpl implements ImpuestoService {

    private final ImpuestoRepository impuestoRepository;

    public ImpuestoServiceImpl(ImpuestoRepository impuestoRepository) {
        this.impuestoRepository = impuestoRepository;
    }

    @Override
    public List<ImpuestoResponse> listar() {
        return impuestoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ImpuestoResponse toResponse(Impuesto impuesto) {
        ImpuestoResponse response = new ImpuestoResponse();
        response.setIdImpuesto(impuesto.getIdImpuesto());
        response.setTipoImpuesto(impuesto.getTipoImpuesto());
        response.setValor(impuesto.getValor());
        return response;
    }
}