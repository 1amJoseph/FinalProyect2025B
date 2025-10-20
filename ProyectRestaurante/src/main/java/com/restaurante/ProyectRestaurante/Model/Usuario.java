package com.restaurante.ProyectRestaurante.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})  // ← AGREGAR
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_usuario;  // ✅ Cambiar a Long para coincidir con bigint

    @NotBlank(message = "Debe ingresar su nombre")
    private String nombre_usuario;

    @NotBlank(message = "Ingrese su telefono")
    private String telefono;

    @NotBlank(message = "Ingrese su Email")
    private String email;

    @NotBlank(message = "Ingrese su clave")
    private String clave;

    // Guardar solo el ID del rol (más simple)
    @Column(name = "id_rol")
    private Integer id_rol;

    // Relación con Rol (opcional, para cuando necesites el objeto completo)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rol", insertable = false, updatable = false)
    private Rol rol;

    public Usuario() {}

    public Usuario(Long id_usuario, String nombre_usuario, String telefono, String email, String clave, Integer id_rol) {
        this.id_usuario = id_usuario;
        this.nombre_usuario = nombre_usuario;
        this.telefono = telefono;
        this.email = email;
        this.clave = clave;
        this.id_rol = id_rol;
    }

    // Getters y Setters
    public Long getIdUsuario() {
        return id_usuario;
    }

    public void setIdUsuario(Long id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombreUsuario() {
        return nombre_usuario;
    }

    public void setNombreUsuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Integer getIdRol() {
        return id_rol;
    }

    public void setIdRol(Integer id_rol) {
        this.id_rol = id_rol;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}