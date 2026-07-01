package com.buenaventura.erp.inventario.consultastock.entity;

import com.buenaventura.erp.articulo.entity.Articulo;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_consulta_stock_movimiento")
public class ConsultaStockMovimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdConsultaStockMovimiento")
    private Long idConsultaStockMovimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdArticulo", nullable = false)
    private Articulo articulo;

    @Column(name = "FechaMovimiento", nullable = false)
    private LocalDateTime fechaMovimiento;

    @Column(name = "Periodo", nullable = false, length = 7)
    private String periodo;

    @Column(name = "Documento", length = 100)
    private String documento;

    @Column(name = "TipoMovimiento", nullable = false, length = 50)
    private String tipoMovimiento;

    @Column(name = "StockInicial", nullable = false, precision = 10, scale = 2)
    private BigDecimal stockInicial;

    @Column(name = "MovimientoCantidad", nullable = false, precision = 10, scale = 2)
    private BigDecimal movimientoCantidad;

    @Column(name = "Saldo", nullable = false, precision = 10, scale = 2)
    private BigDecimal saldo;

    @Column(name = "ReferenciaTipo", length = 30)
    private String referenciaTipo;

    @Column(name = "ReferenciaId")
    private Long referenciaId;

    @Column(name = "FlgActivo")
    private Boolean flgActivo;

    @Column(name = "FechaCreacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "FechaActualizacion")
    private LocalDateTime fechaActualizacion;

    public Long getIdConsultaStockMovimiento() {
        return idConsultaStockMovimiento;
    }

    public void setIdConsultaStockMovimiento(Long idConsultaStockMovimiento) {
        this.idConsultaStockMovimiento = idConsultaStockMovimiento;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
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

    public String getReferenciaTipo() {
        return referenciaTipo;
    }

    public void setReferenciaTipo(String referenciaTipo) {
        this.referenciaTipo = referenciaTipo;
    }

    public Long getReferenciaId() {
        return referenciaId;
    }

    public void setReferenciaId(Long referenciaId) {
        this.referenciaId = referenciaId;
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

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}
