package com.buenaventura.erp.recepciones.dto;

import java.math.BigDecimal;

public class RecepcionDetalleItemResponse {

    private Integer idRecepcionDetalle;
    private Integer idCompraDetalle;
    private Integer idArticulo;
    private Integer idCategoria;
    private String articulo;
    private String descripcionCategoria;
    private String tipoEnvase;
    private String medida;
    private BigDecimal pesoComprado;
    private BigDecimal recibido;
    private BigDecimal totalRecibido;
    private BigDecimal pesoPendiente;
    private BigDecimal costoKilo;
    private BigDecimal costoTotal;
    private String estado;

    public Integer getIdRecepcionDetalle() {
        return idRecepcionDetalle;
    }

    public void setIdRecepcionDetalle(Integer idRecepcionDetalle) {
        this.idRecepcionDetalle = idRecepcionDetalle;
    }

    public Integer getIdCompraDetalle() {
        return idCompraDetalle;
    }

    public void setIdCompraDetalle(Integer idCompraDetalle) {
        this.idCompraDetalle = idCompraDetalle;
    }

    public Integer getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(Integer idArticulo) {
        this.idArticulo = idArticulo;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }

    public String getDescripcionCategoria() {
        return descripcionCategoria;
    }

    public void setDescripcionCategoria(String descripcionCategoria) {
        this.descripcionCategoria = descripcionCategoria;
    }

    public String getTipoEnvase() {
        return tipoEnvase;
    }

    public void setTipoEnvase(String tipoEnvase) {
        this.tipoEnvase = tipoEnvase;
    }

    public String getMedida() {
        return medida;
    }

    public void setMedida(String medida) {
        this.medida = medida;
    }

    public BigDecimal getPesoComprado() {
        return pesoComprado;
    }

    public void setPesoComprado(BigDecimal pesoComprado) {
        this.pesoComprado = pesoComprado;
    }

    public BigDecimal getRecibido() {
        return recibido;
    }

    public void setRecibido(BigDecimal recibido) {
        this.recibido = recibido;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
