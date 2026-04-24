package com.buenaventura.erp.recepciones.entity;

import com.buenaventura.erp.compras.entity.CompraDetalle;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_recepcion_detalle")
public class RecepcionDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdRecepcionDetalle")
    private Integer idRecepcionDetalle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdRecepciones", nullable = false)
    private Recepcion recepcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdCompraDetalle", nullable = false)
    private CompraDetalle compraDetalle;

    @Column(name = "Recibido", nullable = false, precision = 10, scale = 2)
    private BigDecimal recibido;

    @Column(name = "Estado", nullable = false, length = 20)
    private String estado;

    @Column(name = "FlgActivo")
    private Boolean flgActivo;

    @Column(name = "FechaCreacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "FechaActualizacion")
    private LocalDateTime fechaActualizacion;

    public Integer getIdRecepcionDetalle() {
        return idRecepcionDetalle;
    }

    public void setIdRecepcionDetalle(Integer idRecepcionDetalle) {
        this.idRecepcionDetalle = idRecepcionDetalle;
    }

    public Recepcion getRecepcion() {
        return recepcion;
    }

    public void setRecepcion(Recepcion recepcion) {
        this.recepcion = recepcion;
    }

    public CompraDetalle getCompraDetalle() {
        return compraDetalle;
    }

    public void setCompraDetalle(CompraDetalle compraDetalle) {
        this.compraDetalle = compraDetalle;
    }

    public BigDecimal getRecibido() {
        return recibido;
    }

    public void setRecibido(BigDecimal recibido) {
        this.recibido = recibido;
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