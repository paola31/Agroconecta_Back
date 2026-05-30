package com.agroconecta.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 120, message = "El nombre no debe superar 120 caracteres")
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe tener un formato valido")
    @Size(max = 160, message = "El correo no debe superar 160 caracteres")
    private String email;

    @Size(max = 30, message = "El telefono no debe superar 30 caracteres")
    private String telefono;

    @NotBlank(message = "El rol es obligatorio")
    @Pattern(regexp = "campesino|cliente|admin", message = "El rol debe ser campesino, cliente o admin")
    private String rol;

    @NotBlank(message = "La contrasena es obligatoria")
    @Size(min = 6, max = 80, message = "La contrasena debe tener entre 6 y 80 caracteres")
    private String password;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
