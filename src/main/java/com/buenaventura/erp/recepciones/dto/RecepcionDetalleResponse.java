package com.buenaventura.erp.recepciones.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class RecepcionDetalleResponse {

    private Integer idCompras;
    private LocalDateTime fechaCompras;
    private String estado;
    private String razonSocial;
    private String ruc;
    private String zonaProduccion;
    private BigDecimal numeroLote;

    private BigDecimal pesoComprado;
    private BigDecimal totalRecibido;
    private BigDecimal pesoPendiente;

    private BigDecimal costoTotal;

    private List<RecepcionDetalleItemResponse> detalles;

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

    public BigDecimal getPesoComprado() {
        return pesoComprado;
    }

    public void setPesoComprado(BigDecimal pesoComprado) {
        this.pesoComprado = pesoComprado;
    }

    public BigDecimal getTotalRecibido() {
        return totalRecibido;
    }

    public void setTotalRecibido(BigDecimal totalRecibido) {
        this.totalRecibido = totalRecibido;
    }

    public BigDecimal getPesoPendiente() {
        return pesoPendiente;
    }

    public void setPesoPendiente(BigDecimal pesoPendiente) {
        this.pesoPendiente = pesoPendiente;
    }

    public BigDecimal getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
    }

    public List<RecepcionDetalleItemResponse> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<RecepcionDetalleItemResponse> detalles) {
        this.detalles = detalles;
    }
}
