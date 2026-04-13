package com.buenaventura.erp.cuentaspagar.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CuentaPagarDisponibleResponse {

    private Integer idCompras;
    private Integer idRecepciones;
    private String proveedor;
    private String articulo;
    private LocalDateTime fechaCompra;
    private LocalDateTime fechaRecepcion;
    private String zonaProduccion;
    private BigDecimal hectareas;
    private BigDecimal peso;
    private BigDecimal recibido;
    private BigDecimal costoKilo;
    private BigDecimal costoTotal;
    private BigDecimal importeImpuesto;
    private String estadoCompra;
    private String estadoRecepcion;

    public CuentaPagarDisponibleResponse() {
    }

    public CuentaPagarDisponibleResponse(Integer idCompras,
                                         Integer idRecepciones,
                                         String proveedor,
                                         String articulo,
                                         LocalDateTime fechaCompra,
                                         LocalDateTime fechaRecepcion,
                                         String zonaProduccion,
                                         BigDecimal hectareas,
                                         BigDecimal peso,
                                         BigDecimal recibido,
                                         BigDecimal costoKilo,
                                         BigDecimal costoTotal,
                                         BigDecimal importeImpuesto,
                                         String estadoCompra,
                                         String estadoRecepcion) {
        this.idCompras = idCompras;
        this.idRecepciones = idRecepciones;
        this.proveedor = proveedor;
        this.articulo = articulo;
        this.fechaCompra = fechaCompra;
        this.fechaRecepcion = fechaRecepcion;
        this.zonaProduccion = zonaProduccion;
        this.hectareas = hectareas;
        this.peso = peso;
        this.recibido = recibido;
        this.costoKilo = costoKilo;
        this.costoTotal = costoTotal;
        this.importeImpuesto = importeImpuesto;
        this.estadoCompra = estadoCompra;
        this.estadoRecepcion = estadoRecepcion;
    }

    public Integer getIdCompras() {
        return idCompras;
    }

    public void setIdCompras(Integer idCompras) {
        this.idCompras = idCompras;
    }

    public Integer getIdRecepciones() {
        return idRecepciones;
    }

    public void setIdRecepciones(Integer idRecepciones) {
        this.idRecepciones = idRecepciones;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }

    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public LocalDateTime getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(LocalDateTime fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public String getZonaProduccion() {
        return zonaProduccion;
    }

    public void setZonaProduccion(String zonaProduccion) {
        this.zonaProduccion = zonaProduccion;
    }

    public BigDecimal getHectareas() {
        return hectareas;
    }

    public void setHectareas(BigDecimal hectareas) {
        this.hectareas = hectareas;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public BigDecimal getRecibido() {
        return recibido;
    }

    public void setRecibido(BigDecimal recibido) {
        this.recibido = recibido;
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

    public BigDecimal getImporteImpuesto() {
        return importeImpuesto;
    }

    public void setImporteImpuesto(BigDecimal importeImpuesto) {
        this.importeImpuesto = importeImpuesto;
    }

    public String getEstadoCompra() {
        return estadoCompra;
    }

    public void setEstadoCompra(String estadoCompra) {
        this.estadoCompra = estadoCompra;
    }

    public String getEstadoRecepcion() {
        return estadoRecepcion;
    }

    public void setEstadoRecepcion(String estadoRecepcion) {
        this.estadoRecepcion = estadoRecepcion;
    }
}