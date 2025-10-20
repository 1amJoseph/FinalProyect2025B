package com.restaurante.ProyectRestaurante.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})  // ← AGREGAR
public class Recibo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRecibo;

    // Cliente apunta a la misma tabla usuario
    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Usuario idCliente;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario idUsuario;

    private LocalDate fecha;

    // 👇 Relación con DetalleRecibo
    @OneToMany(mappedBy = "recibo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("recibo") // evita serialización infinita
    private List<DetalleRecibo> detalles = new ArrayList<>();


    // Getters y Setters
    public Long getIdRecibo() { return idRecibo; }
    public void setIdRecibo(Long idRecibo) { this.idRecibo = idRecibo; }

    public Usuario getIdCliente() { return idCliente; }
    public void setIdCliente(Usuario idCliente) { this.idCliente = idCliente; }

    public Usuario getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Usuario idUsuario) { this.idUsuario = idUsuario; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public List<DetalleRecibo> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleRecibo> detalles) { this.detalles = detalles; }
}