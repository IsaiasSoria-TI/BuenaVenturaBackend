package com.buenaventura.erp.cuentacontable.dto;

public class CuentaContableRequest {

    private String codigo;
    private String estado;

    public CuentaContableRequest() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}