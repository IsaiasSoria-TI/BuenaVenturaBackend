package com.buenaventura.erp.cuentacontable.dto;

public class CuentaContableResponse {

    private Integer idCuentaContable;
    private String codigo;
    private String descripcion;
    private String estado;

    public CuentaContableResponse() {
    }

    public Integer getIdCuentaContable() {
        return idCuentaContable;
    }

    public void setIdCuentaContable(Integer idCuentaContable) {
        this.idCuentaContable = idCuentaContable;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}