package com.restaurante.ProyectRestaurante.Repository;

import com.restaurante.ProyectRestaurante.Model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}
