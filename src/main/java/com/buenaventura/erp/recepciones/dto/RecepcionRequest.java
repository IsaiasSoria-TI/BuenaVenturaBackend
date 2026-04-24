package com.buenaventura.erp.recepciones.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class RecepcionRequest {

    @NotNull(message = "La compra es obligatoria")
    private Integer idCompras;

    @Valid
    @NotEmpty(message = "Debe agregar al menos un detalle de recepción")
    private List<RecepcionDetalleRequest> detalles;

    public RecepcionRequest() {
    }

    public Integer getIdCompras() {
        return idCompras;
    }

    public void setIdCompras(Integer idCompras) {
        this.idCompras = idCompras;
    }

    public List<RecepcionDetalleRequest> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<RecepcionDetalleRequest> detalles) {
        this.detalles = detalles;
    }
}