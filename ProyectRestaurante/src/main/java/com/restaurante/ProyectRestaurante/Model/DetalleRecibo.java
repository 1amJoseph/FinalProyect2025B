package com.restaurante.ProyectRestaurante.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name="detalle_recibo")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DetalleRecibo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalleRecibo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_recibo", nullable = false)
    private Recibo recibo;

    @Column(name = "id_producto", nullable = false)
    private Integer idProducto;



    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private BigDecimal subtotal;

    // Getters y Setters
    public Long getIdDetalleRecibo() { return idDetalleRecibo; }

    public Recibo getRecibo() { return recibo; }
    public void setRecibo(Recibo recibo) { this.recibo = recibo; }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}
