package com.buenaventura.erp.recepciones.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class RecepcionRequest {

    @NotNull(message = "La compra es obligatoria")
    private Integer idCompras;

    @NotNull(message = "El peso recibido es obligatorio")
    @DecimalMin(value = "0.01", message = "El peso recibido debe ser mayor a 0")
    private BigDecimal recibido;

    public RecepcionRequest() {
    }

    public Integer getIdCompras() {
        return idCompras;
    }

    public void setIdCompras(Integer idCompras) {
        this.idCompras = idCompras;
    }

    public BigDecimal getRecibido() {
        return recibido;
    }

    public void setRecibido(BigDecimal recibido) {
        this.recibido = recibido;
    }
}