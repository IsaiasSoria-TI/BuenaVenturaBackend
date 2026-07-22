package com.buenaventura.erp.cuentacontable.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CuentaContableRequest {

    @NotBlank(message = "El codigo es obligatorio")
    @Size(max = 20, message = "El codigo no puede superar 20 caracteres")
    private String codigo;

    @NotBlank(message = "La descripcion es obligatoria")
    @Size(max = 100, message = "La descripcion no puede superar 100 caracteres")
    private String descripcion;

    @NotBlank(message = "El estado es obligatorio")
    @Size(max = 20, message = "El estado no puede superar 20 caracteres")
    private String estado;

    public CuentaContableRequest() {
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
