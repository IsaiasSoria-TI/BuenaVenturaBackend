package com.buenaventura.erp.inventario.consultastock.dto;

public class MotivoMovimientoKardexResponse {

    private Integer idMotivoMovimiento;
    private String codigo;
    private String nombre;
    private String naturaleza;

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
}
