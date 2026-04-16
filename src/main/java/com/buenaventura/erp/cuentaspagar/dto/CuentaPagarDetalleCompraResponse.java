package com.buenaventura.erp.cuentaspagar.dto;

import java.math.BigDecimal;
import java.util.List;

public class CuentaPagarDetalleCompraResponse {

    private Integer idCompras;
    private Integer numeroOperacion;
    private String ruc;
    private String razonSocial;
    private Integer codArticulo;
    private String descripcionArticulo;
    private BigDecimal importe;
    private BigDecimal deduccionRetencion;
    private String tipoDetRet;
    private Integer porcentajeImpuesto;
    private String condicionPago;
    private String estadoCompra;
    private List<CuentaPagarRecepcionDisponibleResponse> recepcionesDisponibles;

    public CuentaPagarDetalleCompraResponse() {
    }

    public Integer getIdCompras() {
        return idCompras;
    }

    public void setIdCompras(Integer idCompras) {
        this.idCompras = idCompras;
    }

    public Integer getNumeroOperacion() {
        return numeroOperacion;
    }

    public void setNumeroOperacion(Integer numeroOperacion) {
        this.numeroOperacion = numeroOperacion;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public Integer getCodArticulo() {
        return codArticulo;
    }

    public void setCodArticulo(Integer codArticulo) {
        this.codArticulo = codArticulo;
    }

    public String getDescripcionArticulo() {
        return descripcionArticulo;
    }

    public void setDescripcionArticulo(String descripcionArticulo) {
        this.descripcionArticulo = descripcionArticulo;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public BigDecimal getDeduccionRetencion() {
        return deduccionRetencion;
    }

    public void setDeduccionRetencion(BigDecimal deduccionRetencion) {
        this.deduccionRetencion = deduccionRetencion;
    }

    public String getTipoDetRet() {
        return tipoDetRet;
    }

    public void setTipoDetRet(String tipoDetRet) {
        this.tipoDetRet = tipoDetRet;
    }

    public Integer getPorcentajeImpuesto() {
        return porcentajeImpuesto;
    }

    public void setPorcentajeImpuesto(Integer porcentajeImpuesto) {
        this.porcentajeImpuesto = porcentajeImpuesto;
    }

    public String getCondicionPago() {
        return condicionPago;
    }

    public void setCondicionPago(String condicionPago) {
        this.condicionPago = condicionPago;
    }

    public String getEstadoCompra() {
        return estadoCompra;
    }

    public void setEstadoCompra(String estadoCompra) {
        this.estadoCompra = estadoCompra;
    }

    public List<CuentaPagarRecepcionDisponibleResponse> getRecepcionesDisponibles() {
        return recepcionesDisponibles;
    }

    public void setRecepcionesDisponibles(List<CuentaPagarRecepcionDisponibleResponse> recepcionesDisponibles) {
        this.recepcionesDisponibles = recepcionesDisponibles;
    }
}