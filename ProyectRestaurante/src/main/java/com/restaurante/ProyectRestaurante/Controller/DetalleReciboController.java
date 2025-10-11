package com.restaurante.ProyectRestaurante.Controller;

import com.restaurante.ProyectRestaurante.Model.DetalleRecibo;
import com.restaurante.ProyectRestaurante.Repository.DetalleReciboRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/detalles")
@CrossOrigin(origins = "http://localhost:4321")
public class DetalleReciboController {

    @Autowired
    private DetalleReciboRepository detalleReciboRepository;

    @PostMapping
    public DetalleRecibo crear(@RequestBody DetalleRecibo detalle) {
        return detalleReciboRepository.save(detalle);
    }

    @GetMapping
    public List<DetalleRecibo> listar() {
        return detalleReciboRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleRecibo> obtenerPorId(@PathVariable Long id) {
        Optional<DetalleRecibo> detalle = detalleReciboRepository.findById(id);
        return detalle.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetalleRecibo> actualizar(
            @PathVariable Long id,
            @RequestBody DetalleRecibo detalleActualizado) {

        return detalleReciboRepository.findById(id).map(detalle -> {
            detalle.setCantidad(detalleActualizado.getCantidad());
            detalle.setSubtotal(detalleActualizado.getSubtotal());
            detalle.setIdProducto(detalleActualizado.getIdProducto());  // ✅ Usar setIdProducto
            detalle.setIdRecibo(detalleActualizado.getIdRecibo());      // ✅ Usar setIdRecibo

            return ResponseEntity.ok(detalleReciboRepository.save(detalle));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (detalleReciboRepository.existsById(id)) {
            detalleReciboRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

