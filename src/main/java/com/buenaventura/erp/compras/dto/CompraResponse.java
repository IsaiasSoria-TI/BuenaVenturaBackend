package com.buenaventura.erp.compras.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CompraResponse {

    private Integer idCompras;

    private Integer idPago;
    private String pago;

    private Integer idProveedor;
    private String ruc;
    private String razonSocial;
    private String direccion;

    private LocalDateTime fechaCompras;
    private String zonaProduccion;
    private BigDecimal hectareas;

    private BigDecimal peso;
    private BigDecimal costoTotal;
    private BigDecimal importeImpuesto;
    private Boolean aplicaIgv;
    private BigDecimal porcentajeIgv;
    private BigDecimal importeIgv;
    private BigDecimal totalGeneral;

    private String estado;

    private List<CompraDetalleResponse> detalles;
    private List<CompraImpuestoResponse> impuestos;

    public CompraResponse() {
    }

    public Integer getIdCompras() {
        return idCompras;
    }

    public void setIdCompras(Integer idCompras) {
        this.idCompras = idCompras;
    }

    public Integer getIdPago() {
        return idPago;
    }

    public void setIdPago(Integer idPago) {
        this.idPago = idPago;
    }

    public String getPago() {
        return pago;
    }

    public void setPago(String pago) {
        this.pago = pago;
    }

    public Integer getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public LocalDateTime getFechaCompras() {
        return fechaCompras;
    }

    public void setFechaCompras(LocalDateTime fechaCompras) {
        this.fechaCompras = fechaCompras;
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

    public Boolean getAplicaIgv() {
        return aplicaIgv;
    }

    public void setAplicaIgv(Boolean aplicaIgv) {
        this.aplicaIgv = aplicaIgv;
    }

    public BigDecimal getPorcentajeIgv() {
        return porcentajeIgv;
    }

    public void setPorcentajeIgv(BigDecimal porcentajeIgv) {
        this.porcentajeIgv = porcentajeIgv;
    }

    public BigDecimal getImporteIgv() {
        return importeIgv;
    }

    public void setImporteIgv(BigDecimal importeIgv) {
        this.importeIgv = importeIgv;
    }

    public BigDecimal getTotalGeneral() {
        return totalGeneral;
    }

    public void setTotalGeneral(BigDecimal totalGeneral) {
        this.totalGeneral = totalGeneral;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<CompraDetalleResponse> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<CompraDetalleResponse> detalles) {
        this.detalles = detalles;
    }

    public List<CompraImpuestoResponse> getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(List<CompraImpuestoResponse> impuestos) {
        this.impuestos = impuestos;
    }
}
