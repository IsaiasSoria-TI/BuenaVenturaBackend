package com.buenaventura.erp.configuracion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SeguridadUsuarioRequest {

    @NotBlank(message = "El usuario es obligatorio")
    @Size(max = 50, message = "El usuario no puede superar 50 caracteres")
    private String usuario;

    @Size(max = 120, message = "La contrasena no puede superar 120 caracteres")
    private String contrasena;

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 120, message = "Los nombres no pueden superar 120 caracteres")
    private String nombres;

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(max = 35, message = "El apellido paterno no puede superar 35 caracteres")
    private String apellidoPaterno;

    @Size(max = 35, message = "El apellido materno no puede superar 35 caracteres")
    private String apellidoMaterno;

    @Size(max = 9, message = "El telefono no puede superar 9 caracteres")
    private String telefono;

    @Size(max = 8, message = "El DNI no puede superar 8 caracteres")
    private String dni;

    @Size(max = 150, message = "El correo no puede superar 150 caracteres")
    private String correo;

    private Boolean flgActivo;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Boolean getFlgActivo() {
        return flgActivo;
    }

    public void setFlgActivo(Boolean flgActivo) {
        this.flgActivo = flgActivo;
    }
}
