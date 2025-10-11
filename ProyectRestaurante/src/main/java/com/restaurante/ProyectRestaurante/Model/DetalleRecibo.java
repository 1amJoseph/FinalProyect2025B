package com.restaurante.ProyectRestaurante.Model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class DetalleRecibo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalleRecibo;

    @Column(name = "id_recibo")
    private Long idRecibo;

    @Column(name = "id_producto")
    private Integer idProducto;

    private Integer cantidad;

    private BigDecimal subtotal;

    // Getters y Setters
    public Long getIdDetalleRecibo() { return idDetalleRecibo; }
    public void setIdDetalleRecibo(Long idDetalleRecibo) { this.idDetalleRecibo = idDetalleRecibo; }

    public Long getIdRecibo() { return idRecibo; }
    public void setIdRecibo(Long idRecibo) { this.idRecibo = idRecibo; }

    public Integer getIdProducto() { return idProducto; }
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}