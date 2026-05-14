package com.buenaventura.erp.proveedores.banco.dto;

import jakarta.validation.constraints.NotBlank;

public class BancoRequest {

    @NotBlank(message = "El nombre del banco es obligatorio")
    private String nombre;

    private Boolean flgActivo;

    public BancoRequest() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getFlgActivo() {
        return flgActivo;
    }

    public void setFlgActivo(Boolean flgActivo) {
        this.flgActivo = flgActivo;
    }
}
