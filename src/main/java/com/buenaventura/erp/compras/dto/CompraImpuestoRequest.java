package com.buenaventura.erp.compras.dto;

import jakarta.validation.constraints.NotNull;

public class CompraImpuestoRequest {

    @NotNull(message = "El impuesto es obligatorio")
    private Integer idImpuesto;

    public CompraImpuestoRequest() {
    }

    public Integer getIdImpuesto() {
        return idImpuesto;
    }

    public void setIdImpuesto(Integer idImpuesto) {
        this.idImpuesto = idImpuesto;
    }
}