package com.restaurante.ProyectRestaurante.Repository;

import com.restaurante.ProyectRestaurante.Model.Recibo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReciboRepository extends JpaRepository<Recibo, Long> {

    // Esta consulta fuerza a cargar detalles y productos
    @Query("SELECT r FROM Recibo r JOIN FETCH r.detalles d JOIN FETCH d.idProducto")
    List<Recibo> findAllWithDetallesAndProductos();
}
