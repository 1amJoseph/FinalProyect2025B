package com.restaurante.ProyectRestaurante.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_mesa;

    @NotNull(message = "Debe ingrese el numero de mesa")
    private int numero_mesa;

    @NotNull(message = "Capacidad de la mesa")
    private int capacidad;

    @NotNull(message = "Disponibilidad de la mesa ")
    private Boolean disponibilidad;

    public Mesa(){}

    public Mesa(Integer id_mesa, int numero_mesa, int capacidad, Boolean disponibilidad) {
        this.id_mesa = id_mesa;
        this.numero_mesa = numero_mesa;
        this.capacidad = capacidad;
        this.disponibilidad = disponibilidad;
    }

    public Integer getId_mesa() {
        return id_mesa;
    }

    public void setId_mesa(Integer id_mesa) {
        this.id_mesa = id_mesa;
    }

    public int getNumero_mesa() {
        return numero_mesa;
    }

    public void setNumero_mesa(int numero_mesa) {
        this.numero_mesa = numero_mesa;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public Boolean getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(Boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }
}
