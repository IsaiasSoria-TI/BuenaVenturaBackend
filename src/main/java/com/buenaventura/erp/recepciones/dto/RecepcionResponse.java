package com.buenaventura.erp.recepciones.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class RecepcionResponse {

    private Integer idRecepciones;
    private Integer idCompras;
    private LocalDateTime fechaRecepcion;
    private String guiaRemision;
    private BigDecimal cantidadJabas;
    private String estado;

    private String estadoCompra;
    private String razonSocial;
    private String ruc;

    private BigDecimal pesoComprado;
    private BigDecimal recibido;

    private String articulo;
    private String medida;

    private List<RecepcionDetalleItemResponse> detalles;

    public Integer getIdRecepciones() {
        return idRecepciones;
    }

    public void setIdRecepciones(Integer idRecepciones) {
        this.idRecepciones = idRecepciones;
    }

    public Integer getIdCompras() {
        return idCompras;
    }

    public void setIdCompras(Integer idCompras) {
        this.idCompras = idCompras;
    }

    public LocalDateTime getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(LocalDateTime fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public String getGuiaRemision() {
        return guiaRemision;
    }

    public void setGuiaRemision(String guiaRemision) {
        this.guiaRemision = guiaRemision;
    }

    public BigDecimal getCantidadJabas() {
        return cantidadJabas;
    }

    public void setCantidadJabas(BigDecimal cantidadJabas) {
        this.cantidadJabas = cantidadJabas;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstadoCompra() {
        return estadoCompra;
    }

    public void setEstadoCompra(String estadoCompra) {
        this.estadoCompra = estadoCompra;
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

    public List<RecepcionDetalleItemResponse> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<RecepcionDetalleItemResponse> detalles) {
        this.detalles = detalles;
    }
}
