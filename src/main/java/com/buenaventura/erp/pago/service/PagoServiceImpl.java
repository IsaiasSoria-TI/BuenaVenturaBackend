package com.buenaventura.erp.pago.service;

import com.buenaventura.erp.pago.dto.PagoResponse;
import com.buenaventura.erp.pago.entity.Pago;
import com.buenaventura.erp.pago.repository.PagoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;

    public PagoServiceImpl(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    @Override
    public List<PagoResponse> listar() {
        return pagoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private PagoResponse toResponse(Pago pago) {
        PagoResponse response = new PagoResponse();
        response.setIdPago(pago.getIdPago());
        response.setPago(pago.getPago());
        response.setDias(pago.getDias());
        return response;
    }
}