package com.buenaventura.erp.impuesto.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ImpuestoRequest {

    @NotBlank(message = "El tipo de impuesto es obligatorio")
    private String tipoImpuesto;

    @NotNull(message = "El valor es obligatorio")
    @DecimalMin(value = "0.00", inclusive = true, message = "El valor no puede ser negativo")
    @Digits(integer = 8, fraction = 2, message = "El valor debe tener máximo 2 decimales")
    private BigDecimal valor;

    private Boolean flgActivo;

    public ImpuestoRequest() {
    }

    public String getTipoImpuesto() {
        return tipoImpuesto;
    }

    public void setTipoImpuesto(String tipoImpuesto) {
        this.tipoImpuesto = tipoImpuesto;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Boolean getFlgActivo() {
        return flgActivo;
    }

    public void setFlgActivo(Boolean flgActivo) {
        this.flgActivo = flgActivo;
    }
}
