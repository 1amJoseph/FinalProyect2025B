package com.restaurante.ProyectRestaurante.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_producto;

    @NotBlank(message = "Nombre del producto?")
    private String nombre_producto;

    @NotBlank(message = "y con ganas de vender a cuanto?")
    private Double precio_unitario;

    public Producto(){}

    public Producto(Integer id_producto, String nombre_producto, Double precio_unitario) {
        this.id_producto = id_producto;
        this.nombre_producto = nombre_producto;
        this.precio_unitario = precio_unitario;
    }

    public Integer getId_producto() {
        return id_producto;
    }

    public void setId_producto(Integer id_producto) {
        this.id_producto = id_producto;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public Double getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(Double precio_unitario) {
        this.precio_unitario = precio_unitario;
    }
}