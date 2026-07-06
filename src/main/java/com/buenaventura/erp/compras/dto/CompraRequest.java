package com.buenaventura.erp.compras.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CompraRequest {

    @NotNull(message = "La condición de pago es obligatoria")
    private Integer idPago;

    @NotNull(message = "La moneda es obligatoria")
    private Integer idMoneda;

    private Integer idTipoCambio;
    private BigDecimal tipoCambioAplicado;

    @NotNull(message = "El proveedor es obligatorio")
    private Integer idProveedor;

    @NotNull(message = "La fecha de compra es obligatoria")
    private LocalDateTime fechaCompras;

    private LocalDateTime fechaEmision;
    private LocalDateTime fechaIngresoProducto;
    private String tipoDocumento;
    private String numeroDocumentoProveedor;
    private String serieReferencia;
    private String correlativoReferencia;
    private String observacion;

    @NotBlank(message = "La zona de producción es obligatoria")
    private String zonaProduccion;

    @NotNull(message = "El número de lotes es obligatorio")
    @DecimalMin(value = "0.00", inclusive = true, message = "El número de lotes no puede ser negativo")
    private BigDecimal numeroLote;

    @Valid
    private List<CompraDetalleRequest> detalles;

    @Valid
    private List<CompraImpuestoRequest> impuestos;

    private Boolean aplicaIgv;
    private BigDecimal porcentajeIgv;

    public CompraRequest() {
    }

    public Integer getIdPago() {
        return idPago;
    }

    public void setIdPago(Integer idPago) {
        this.idPago = idPago;
    }

    public Integer getIdMoneda() {
        return idMoneda;
    }

    public void setIdMoneda(Integer idMoneda) {
        this.idMoneda = idMoneda;
    }

    public Integer getIdTipoCambio() {
        return idTipoCambio;
    }

    public void setIdTipoCambio(Integer idTipoCambio) {
        this.idTipoCambio = idTipoCambio;
    }

    public BigDecimal getTipoCambioAplicado() {
        return tipoCambioAplicado;
    }

    public void setTipoCambioAplicado(BigDecimal tipoCambioAplicado) {
        this.tipoCambioAplicado = tipoCambioAplicado;
    }

    public Integer getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }

    public LocalDateTime getFechaCompras() {
        return fechaCompras;
    }

    public void setFechaCompras(LocalDateTime fechaCompras) {
        this.fechaCompras = fechaCompras;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public LocalDateTime getFechaIngresoProducto() {
        return fechaIngresoProducto;
    }

    public void setFechaIngresoProducto(LocalDateTime fechaIngresoProducto) {
        this.fechaIngresoProducto = fechaIngresoProducto;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumentoProveedor() {
        return numeroDocumentoProveedor;
    }

    public void setNumeroDocumentoProveedor(String numeroDocumentoProveedor) {
        this.numeroDocumentoProveedor = numeroDocumentoProveedor;
    }

    public String getSerieReferencia() {
        return serieReferencia;
    }

    public void setSerieReferencia(String serieReferencia) {
        this.serieReferencia = serieReferencia;
    }

    public String getCorrelativoReferencia() {
        return correlativoReferencia;
    }

    public void setCorrelativoReferencia(String correlativoReferencia) {
        this.correlativoReferencia = correlativoReferencia;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
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

    public List<CompraDetalleRequest> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<CompraDetalleRequest> detalles) {
        this.detalles = detalles;
    }

    public List<CompraImpuestoRequest> getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(List<CompraImpuestoRequest> impuestos) {
        this.impuestos = impuestos;
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
}
