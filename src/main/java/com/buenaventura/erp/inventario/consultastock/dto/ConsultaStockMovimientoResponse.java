package com.buenaventura.erp.inventario.consultastock.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ConsultaStockMovimientoResponse {

    private Long idConsultaStockMovimiento;
    private Integer idArticulo;
    private String descripcionArticulo;
    private LocalDateTime fechaMovimiento;
    private String periodo;
    private String documento;
    private String tipoMovimiento;
    private BigDecimal stockInicial;
    private BigDecimal movimientoCantidad;
    private BigDecimal saldo;

    public Long getIdConsultaStockMovimiento() {
        return idConsultaStockMovimiento;
    }

    public void setIdConsultaStockMovimiento(Long idConsultaStockMovimiento) {
        this.idConsultaStockMovimiento = idConsultaStockMovimiento;
    }

    public Integer getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(Integer idArticulo) {
        this.idArticulo = idArticulo;
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

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
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
