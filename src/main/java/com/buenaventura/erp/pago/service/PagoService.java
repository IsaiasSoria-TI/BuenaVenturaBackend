package com.buenaventura.erp.pago.service;

import com.buenaventura.erp.pago.dto.PagoResponse;

import java.util.List;

public interface PagoService {
    List<PagoResponse> listar();
}