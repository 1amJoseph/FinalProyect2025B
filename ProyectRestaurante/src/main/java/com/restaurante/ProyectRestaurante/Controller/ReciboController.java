package com.restaurante.ProyectRestaurante.Controller;

import com.restaurante.ProyectRestaurante.Model.Recibo;
import com.restaurante.ProyectRestaurante.Model.DetalleRecibo;
import com.restaurante.ProyectRestaurante.Model.Usuario;
import com.restaurante.ProyectRestaurante.Repository.ReciboRepository;
import com.restaurante.ProyectRestaurante.Repository.DetalleReciboRepository;
import com.restaurante.ProyectRestaurante.Repository.UsuarioRepository;
import jakarta.transaction.Transactional;
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

    @Transactional
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Map<String, Object> reciboData) {
        try {
            // Obtener IDs
            Long idCliente = ((Number) reciboData.get("id_cliente")).longValue();
            Long idUsuario = ((Number) reciboData.get("id_usuario")).longValue();

            // Buscar entidades Usuario
            Usuario cliente = usuarioRepository.findById(idCliente)
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            Usuario usuario = usuarioRepository.findById(idUsuario)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Crear recibo
            Recibo recibo = new Recibo();
            recibo.setIdCliente(cliente);
            recibo.setIdUsuario(usuario);
            recibo.setFecha(LocalDate.parse((String) reciboData.get("fecha")));

            // Crear detalles del recibo
            List<Map<String, Object>> detalles = (List<Map<String, Object>>) reciboData.get("detalles");
            if (detalles != null && !detalles.isEmpty()) {
                for (Map<String, Object> detalle : detalles) {
                    DetalleRecibo detalleRecibo = new DetalleRecibo();
                    detalleRecibo.setRecibo(recibo); // vínculo con el recibo
                    detalleRecibo.setIdProducto(((Number) detalle.get("id_producto")).intValue());
                    detalleRecibo.setCantidad(((Number) detalle.get("cantidad")).intValue());

                    // Manejo robusto del subtotal
                    Object subtotalObj = detalle.get("subtotal");
                    BigDecimal subtotal;
                    if (subtotalObj instanceof String) {
                        subtotal = new BigDecimal((String) subtotalObj);
                    } else if (subtotalObj instanceof Number) {
                        subtotal = BigDecimal.valueOf(((Number) subtotalObj).doubleValue());
                    } else {
                        subtotal = BigDecimal.ZERO;
                    }
                    detalleRecibo.setSubtotal(subtotal);

                    // Agregar detalle a la lista del recibo
                    recibo.getDetalles().add(detalleRecibo);
                }
            }

            // Guardar recibo y detalles automáticamente
            Recibo savedRecibo = reciboRepository.save(recibo);

            return ResponseEntity.ok(savedRecibo);

        } catch (Exception e) {
            e.printStackTrace();
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