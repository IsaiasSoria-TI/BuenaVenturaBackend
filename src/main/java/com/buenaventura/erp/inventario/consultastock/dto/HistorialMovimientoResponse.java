package com.buenaventura.erp.inventario.consultastock.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class HistorialMovimientoResponse {

    private Long idMovimiento;
    private Integer idArticulo;
    private String codigoArticulo;
    private String descripcionArticulo;
    private LocalDateTime fechaMovimiento;
    private LocalDateTime fechaTransaccion;
    private String periodo;
    private String documento;
    private String codigoMovimiento;
    private String tipoMovimiento;
    private String proveedorMotivo;
    private String detalle;
    private BigDecimal totalSoles;
    private BigDecimal stockInicial;
    private BigDecimal movimientoCantidad;
    private BigDecimal saldo;

    public Long getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(Long idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public Integer getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(Integer idArticulo) {
        this.idArticulo = idArticulo;
    }

    public String getCodigoArticulo() {
        return codigoArticulo;
    }

    public void setCodigoArticulo(String codigoArticulo) {
        this.codigoArticulo = codigoArticulo;
    }

    public String getDescripcionArticulo() {
        return descripcionArticulo;
    }

    public void setDescripcionArticulo(String descripcionArticulo) {
        this.descripcionArticulo = descripcionArticulo;
    }

    public LocalDateTime getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(LocalDateTime fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public LocalDateTime getFechaTransaccion() {
        return fechaTransaccion;
    }

    public void setFechaTransaccion(LocalDateTime fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getCodigoMovimiento() {
        return codigoMovimiento;
    }

    public void setCodigoMovimiento(String codigoMovimiento) {
        this.codigoMovimiento = codigoMovimiento;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public String getProveedorMotivo() {
        return proveedorMotivo;
    }

    public void setProveedorMotivo(String proveedorMotivo) {
        this.proveedorMotivo = proveedorMotivo;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public BigDecimal getTotalSoles() {
        return totalSoles;
    }

    public void setTotalSoles(BigDecimal totalSoles) {
        this.totalSoles = totalSoles;
    }

    public BigDecimal getStockInicial() {
        return stockInicial;
    }

    public void setStockInicial(BigDecimal stockInicial) {
        this.stockInicial = stockInicial;
    }

    public BigDecimal getMovimientoCantidad() {
        return movimientoCantidad;
    }

    public void setMovimientoCantidad(BigDecimal movimientoCantidad) {
        this.movimientoCantidad = movimientoCantidad;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}
