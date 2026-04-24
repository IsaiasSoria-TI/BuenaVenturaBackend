package com.buenaventura.erp.cuentaspagar.entity;

import com.buenaventura.erp.recepciones.entity.RecepcionDetalle;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_cuenta_pagar_detalle")
public class CuentaPagarDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdCuentaPagarDetalle")
    private Integer idCuentaPagarDetalle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdCuentaPagar", nullable = false)
    private CuentaPagar cuentaPagar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdRecepcionDetalle", nullable = false)
    private RecepcionDetalle recepcionDetalle;

    @Column(name = "Importe", nullable = false, precision = 10, scale = 2)
    private BigDecimal importe;

    @Column(name = "Estado", nullable = false, length = 20)
    private String estado;

    @Column(name = "FlgActivo")
    private Boolean flgActivo;

    @Column(name = "FechaCreacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "FechaActualizacion")
    private LocalDateTime fechaActualizacion;

    public Integer getIdCuentaPagarDetalle() {
        return idCuentaPagarDetalle;
    }

    public void setIdCuentaPagarDetalle(Integer idCuentaPagarDetalle) {
        this.idCuentaPagarDetalle = idCuentaPagarDetalle;
    }

    public CuentaPagar getCuentaPagar() {
        return cuentaPagar;
    }

    public void setCuentaPagar(CuentaPagar cuentaPagar) {
        this.cuentaPagar = cuentaPagar;
    }

    public RecepcionDetalle getRecepcionDetalle() {
        return recepcionDetalle;
    }

    public void setRecepcionDetalle(RecepcionDetalle recepcionDetalle) {
        this.recepcionDetalle = recepcionDetalle;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
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

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}