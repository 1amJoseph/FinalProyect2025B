package com.restaurante.ProyectRestaurante.Controller;

import com.restaurante.ProyectRestaurante.Model.Mesa;
import com.restaurante.ProyectRestaurante.Repository.MesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/mesas")
@CrossOrigin(
        origins = "http://localhost:4321",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}
)
public class MesaController {

    public static class DisponibilidadDTO {
        private boolean disponibilidad;
        public boolean isDisponibilidad() { return disponibilidad; }
        public void setDisponibilidad(boolean disponibilidad) { this.disponibilidad = disponibilidad; }
    }


    @Autowired
    private MesaRepository mesaRepository;

    @PostMapping
    public Mesa crear(@RequestBody Mesa mesa) {
        return mesaRepository.save(mesa);
    }

    @GetMapping
    public List<Mesa> listar() {
        return mesaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mesa> obtenerPorId(@PathVariable Integer id) {
        Optional<Mesa> mesa = mesaRepository.findById(id);
        return mesa.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }



    @PutMapping("/{id}")
    public ResponseEntity<Mesa> toggleDisponibilidad(@PathVariable Integer id, @RequestBody Map<String, Boolean> body) {
        return mesaRepository.findById(id).map(mesa -> {
            mesa.setDisponibilidad(body.get("disponibilidad"));
            return ResponseEntity.ok(mesaRepository.save(mesa));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }






    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (mesaRepository.existsById(id)) {
            mesaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
