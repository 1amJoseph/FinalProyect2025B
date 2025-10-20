package com.restaurante.ProyectRestaurante.Repository;

import com.restaurante.ProyectRestaurante.Model.DetalleRecibo;
import com.restaurante.ProyectRestaurante.Model.Recibo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleReciboRepository extends JpaRepository<DetalleRecibo, Long> {
//    List<DetalleRecibo> findByIdRecibo(Long idRecibo);
    List<DetalleRecibo> findByRecibo(Recibo recibo);
}