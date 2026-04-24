package com.buenaventura.erp.compras.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CompraDetalleRequest {

    @NotNull(message = "El artículo es obligatorio")
    private Integer idArticulo;

    @NotNull(message = "El peso es obligatorio")
    @DecimalMin(value = "0.01", message = "El peso debe ser mayor a 0")
    private BigDecimal peso;

    @NotNull(message = "El costo por kilo es obligatorio")
    @DecimalMin(value = "0.01", message = "El costo por kilo debe ser mayor a 0")
    private BigDecimal costoKilo;

    public CompraDetalleRequest() {
    }

    public Integer getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(Integer idArticulo) {
        this.idArticulo = idArticulo;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public BigDecimal getCostoKilo() {
        return costoKilo;
    }

    public void setCostoKilo(BigDecimal costoKilo) {
        this.costoKilo = costoKilo;
    }
}