package com.buenaventura.erp.compras.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CompraRequest {

    @NotNull(message = "La condición de pago es obligatoria")
    private Integer idPago;

    @NotNull(message = "La moneda es obligatoria")
    private Integer idMoneda;

    @NotNull(message = "El proveedor es obligatorio")
    private Integer idProveedor;

    @NotNull(message = "La fecha de compra es obligatoria")
    private LocalDateTime fechaCompras;

    @NotBlank(message = "La zona de producción es obligatoria")
    private String zonaProduccion;

    @NotNull(message = "El número de lotes es obligatorio")
    @DecimalMin(value = "0.00", inclusive = true, message = "El número de lotes no puede ser negativo")
    private BigDecimal numeroLote;

    @Valid
    @NotEmpty(message = "Debe agregar al menos un artículo")
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
