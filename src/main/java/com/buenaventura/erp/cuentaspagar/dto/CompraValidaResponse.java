package com.buenaventura.erp.cuentaspagar.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CompraValidaResponse {

    private Integer idCompras;
    private LocalDateTime fechaCompras;
    private BigDecimal pesoComprado;
    private String estado;
    private String razonSocial;
    private String ruc;
    private String articulo;
    private String medida;
    private String zonaProduccion;
    private BigDecimal numeroLote;
    private BigDecimal costoKilo;
    private BigDecimal costoTotal;

    public CompraValidaResponse() {
    }

    public Integer getIdCompras() {
        return idCompras;
    }

    public void setIdCompras(Integer idCompras) {
        this.idCompras = idCompras;
    }

    public LocalDateTime getFechaCompras() {
        return fechaCompras;
    }

    public void setFechaCompras(LocalDateTime fechaCompras) {
        this.fechaCompras = fechaCompras;
    }

    public BigDecimal getPesoComprado() {
        return pesoComprado;
    }

    public void setPesoComprado(BigDecimal pesoComprado) {
        this.pesoComprado = pesoComprado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }

    public String getMedida() {
        return medida;
    }

    public void setMedida(String medida) {
        this.medida = medida;
    }

    public String getZonaProduccion() {
        return zonaProduccion;
    }

    public void setZonaProduccion(String zonaProduccion) {
        this.zonaProduccion = zonaProduccion;
    }

    public BigDecimal getNumeroLote() {
        return numeroLote;
    }

    public void setNumeroLote(BigDecimal numeroLote) {
        this.numeroLote = numeroLote;
    }

    public BigDecimal getCostoKilo() {
        return costoKilo;
    }

    public void setCostoKilo(BigDecimal costoKilo) {
        this.costoKilo = costoKilo;
    }

    public BigDecimal getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
    }
}
