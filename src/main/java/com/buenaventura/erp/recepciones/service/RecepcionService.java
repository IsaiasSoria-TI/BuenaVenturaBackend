package com.buenaventura.erp.recepciones.service;

import com.buenaventura.erp.recepciones.dto.RecepcionDetalleResponse;
import com.buenaventura.erp.recepciones.dto.RecepcionDatosRequest;
import com.buenaventura.erp.recepciones.dto.RecepcionRequest;
import com.buenaventura.erp.recepciones.dto.RecepcionResponse;

import java.util.List;

public interface RecepcionService {

    List<RecepcionResponse> listar();

    RecepcionResponse registrar(RecepcionRequest request);

    RecepcionResponse actualizarDatos(Integer id, RecepcionDatosRequest request);

    List<RecepcionResponse> listarComprasPendientes();

    RecepcionDetalleResponse verDetalleCompra(Integer idCompras);

    void eliminarLogico(Integer id);
}
