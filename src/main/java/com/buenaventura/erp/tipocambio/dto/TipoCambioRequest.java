package com.buenaventura.erp.tipocambio.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TipoCambioRequest {

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "El valor es obligatorio")
    @DecimalMin(value = "0.0001", inclusive = true, message = "El valor debe ser mayor a 0")
    @Digits(integer = 6, fraction = 4, message = "El valor debe tener maximo 4 decimales")
    private BigDecimal valor;

    private Boolean flgActivo;

    public TipoCambioRequest() {
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
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
