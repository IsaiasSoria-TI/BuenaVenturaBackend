package com.buenaventura.erp.configuracion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class PerfilUpdateRequest {

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 120, message = "Los nombres no deben exceder 120 caracteres")
    private String nombres;

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(max = 35, message = "El apellido paterno no debe exceder 35 caracteres")
    private String apellidoPaterno;

    @NotBlank(message = "El apellido materno es obligatorio")
    @Size(max = 35, message = "El apellido materno no debe exceder 35 caracteres")
    private String apellidoMaterno;

    @NotBlank(message = "El usuario es obligatorio")
    @Size(max = 50, message = "El usuario no debe exceder 50 caracteres")
    private String usuario;

    @Size(max = 9, message = "El teléfono no debe exceder 9 caracteres")
    @Pattern(regexp = "^$|^[0-9]{9}$", message = "El teléfono debe tener 9 dígitos")
    private String telefono;

    @Size(max = 150, message = "El correo no debe exceder 150 caracteres")
    private String correo;

    public PerfilUpdateRequest() {
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}