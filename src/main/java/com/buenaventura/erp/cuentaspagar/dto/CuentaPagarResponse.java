package com.buenaventura.erp.cuentaspagar.dto;

import java.time.LocalDateTime;
import java.util.List;

public class CuentaPagarResponse {

    private Integer idCuentaPagar;
    private Integer idCompras;
    private Integer idRecepciones;
    private String numeroFactura;
    private String moneda;
    private String codigoDetRet;
    private String estado;
    private Boolean flgActivo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private String proveedor;
    private String ruc;
    private String articulo;
    private String estadoRecepcion;
    private List<CuentaPagarDetalleResponse> detalles;

    public CuentaPagarResponse() {
    }

    public Integer getIdCuentaPagar() {
        return idCuentaPagar;
    }

    public void setIdCuentaPagar(Integer idCuentaPagar) {
        this.idCuentaPagar = idCuentaPagar;
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

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getCodigoDetRet() {
        return codigoDetRet;
    }

    public void setCodigoDetRet(String codigoDetRet) {
        this.codigoDetRet = codigoDetRet;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Boolean getFlgActivo() {
        return flgActivo;
    }

    public void setFlgActivo(Boolean flgActivo) {
        this.flgActivo = flgActivo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
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

    public String getEstadoRecepcion() {
        return estadoRecepcion;
    }

    public void setEstadoRecepcion(String estadoRecepcion) {
        this.estadoRecepcion = estadoRecepcion;
    }

    public List<CuentaPagarDetalleResponse> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<CuentaPagarDetalleResponse> detalles) {
        this.detalles = detalles;
    }
}