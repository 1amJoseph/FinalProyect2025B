package com.restaurante.ProyectRestaurante.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;


@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})  // ← AGREGAR
public class Rol {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id_rol;

    //@NotBlank(message = "Ingrese su Rol")
    private String nombre_rol;

    public Rol(){}

    public Rol(Integer id_rol, String nombre_rol) {
        this.id_rol = id_rol;
        this.nombre_rol = nombre_rol;
    }

    public Integer getId_rol() {
        return id_rol;
    }

    public void setId_rol(Integer id_rol) {
        this.id_rol = id_rol;
    }

    public String getNombre_rol() {
        return nombre_rol;
    }

    public void setNombre_rol(String nombre_rol) {
        this.nombre_rol = nombre_rol;
    }
}
