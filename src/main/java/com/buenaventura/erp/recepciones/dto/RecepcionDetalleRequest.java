package com.buenaventura.erp.recepciones.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class RecepcionDetalleRequest {

    @NotNull(message = "El detalle de compra es obligatorio")
    private Integer idCompraDetalle;

    @NotNull(message = "El peso recibido es obligatorio")
    @DecimalMin(value = "0.01", message = "El peso recibido debe ser mayor a 0")
    private BigDecimal recibido;

    public RecepcionDetalleRequest() {
    }

    public Integer getIdCompraDetalle() {
        return idCompraDetalle;
    }

    public void setIdCompraDetalle(Integer idCompraDetalle) {
        this.idCompraDetalle = idCompraDetalle;
    }

    public BigDecimal getRecibido() {
        return recibido;
    }

    public void setRecibido(BigDecimal recibido) {
        this.recibido = recibido;
    }
}