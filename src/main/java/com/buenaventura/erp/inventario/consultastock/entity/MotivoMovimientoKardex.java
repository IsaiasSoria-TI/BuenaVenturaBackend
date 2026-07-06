package com.buenaventura.erp.inventario.consultastock.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_motivo_movimiento_kardex")
public class MotivoMovimientoKardex {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdMotivoMovimiento")
    private Integer idMotivoMovimiento;

    @Column(name = "Codigo", nullable = false, length = 50)
    private String codigo;

    @Column(name = "Nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "Naturaleza", nullable = false, length = 20)
    private String naturaleza;

    @Column(name = "FlgActivo")
    private Boolean flgActivo;

    public Integer getIdMotivoMovimiento() {
        return idMotivoMovimiento;
    }

    public void setIdMotivoMovimiento(Integer idMotivoMovimiento) {
        this.idMotivoMovimiento = idMotivoMovimiento;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNaturaleza() {
        return naturaleza;
    }

    public void setNaturaleza(String naturaleza) {
        this.naturaleza = naturaleza;
    }

    public Boolean getFlgActivo() {
        return flgActivo;
    }

    public void setFlgActivo(Boolean flgActivo) {
        this.flgActivo = flgActivo;
    }
}
