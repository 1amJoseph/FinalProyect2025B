package com.restaurante.ProyectRestaurante.Controller;

import com.restaurante.ProyectRestaurante.Model.Recibo;
import com.restaurante.ProyectRestaurante.Model.DetalleRecibo;
import com.restaurante.ProyectRestaurante.Repository.ReciboRepository;
import com.restaurante.ProyectRestaurante.Repository.DetalleReciboRepository;
import com.restaurante.ProyectRestaurante.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/recibos")
@CrossOrigin(origins = "http://localhost:4321")
public class ReciboController {

    @Autowired
    private ReciboRepository reciboRepository;

    @Autowired
    private DetalleReciboRepository detalleReciboRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // ✅ NUEVO: Crear recibo con detalles (para el carrito)
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Map<String, Object> reciboData) {
        try {
            // Validar que el usuario existe
            Long idCliente = ((Number) reciboData.get("id_cliente")).longValue();
            Long idUsuario = ((Number) reciboData.get("id_usuario")).longValue();

            if (!usuarioRepository.existsById(idCliente)) {
                return ResponseEntity.badRequest().body("Usuario (cliente) no encontrado");
            }

            if (!usuarioRepository.existsById(idUsuario)) {
                return ResponseEntity.badRequest().body("Usuario no encontrado");
            }

            // Crear recibo
            Recibo recibo = new Recibo();
            recibo.setIdCliente(idCliente);
            recibo.setIdUsuario(idUsuario);
            recibo.setFecha(LocalDate.parse((String) reciboData.get("fecha")));
            recibo.setIdMesa((Integer) reciboData.get("id_mesa"));

            Recibo savedRecibo = reciboRepository.save(recibo);

            // Crear detalles del recibo
            List<Map<String, Object>> detalles = (List<Map<String, Object>>) reciboData.get("detalles");

            if (detalles != null && !detalles.isEmpty()) {
                for (Map<String, Object> detalle : detalles) {
                    DetalleRecibo detalleRecibo = new DetalleRecibo();
                    detalleRecibo.setIdRecibo(savedRecibo.getIdRecibo());
                    detalleRecibo.setIdProducto((Integer) detalle.get("id_producto"));
                    detalleRecibo.setCantidad((Integer) detalle.get("cantidad"));
                    detalleRecibo.setSubtotal(new BigDecimal((String) detalle.get("subtotal")));

                    detalleReciboRepository.save(detalleRecibo);
                }
            }

            return ResponseEntity.ok(savedRecibo);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al crear recibo: " + e.getMessage());
        }
    }

    @GetMapping
    public List<Recibo> listar() {
        return reciboRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recibo> obtenerPorId(@PathVariable Long id) {
        Optional<Recibo> recibo = reciboRepository.findById(id);
        return recibo.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recibo> actualizar(@PathVariable Long id, @RequestBody Recibo reciboActualizado) {
        return reciboRepository.findById(id).map(recibo -> {
            recibo.setFecha(reciboActualizado.getFecha());
            recibo.setIdCliente(reciboActualizado.getIdCliente());
            recibo.setIdUsuario(reciboActualizado.getIdUsuario());
            recibo.setIdMesa(reciboActualizado.getIdMesa());
            return ResponseEntity.ok(reciboRepository.save(recibo));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (reciboRepository.existsById(id)) {
            reciboRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}