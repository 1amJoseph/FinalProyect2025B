package com.restaurante.ProyectRestaurante.Controller;

import com.restaurante.ProyectRestaurante.Model.Cliente;
import com.restaurante.ProyectRestaurante.Repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "http://localhost:4321")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @PostMapping
    public Cliente crear(@RequestBody Cliente cliente){
        return clienteRepository.save(cliente);
    }

    @GetMapping
    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> ObtenerPorId (@PathVariable Long id){
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.map(ResponseEntity:: ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizar(@PathVariable Long id, @RequestBody Cliente clienteActualizado){
        return clienteRepository.findById(id).map(cliente -> {
            cliente.setNombre_cliente(clienteActualizado.getNombre_cliente());
            cliente.setTelefono(clienteActualizado.getTelefono());
            return ResponseEntity.ok(clienteRepository.save(cliente));
        }).orElseGet(()-> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.notFound().build();
    }
}
