package com.restaurante.ProyectRestaurante.Repository;

import com.restaurante.ProyectRestaurante.Model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {  // ✅ Cambiar Integer a Long si cliente también es bigint
}