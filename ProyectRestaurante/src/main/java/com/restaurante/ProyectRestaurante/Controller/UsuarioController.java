package com.restaurante.ProyectRestaurante.Controller;

import com.restaurante.ProyectRestaurante.Model.Usuario;
import com.restaurante.ProyectRestaurante.Model.Rol;
import com.restaurante.ProyectRestaurante.Repository.UsuarioRepository;
import com.restaurante.ProyectRestaurante.Repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/usuarios")  // ✅ Cambiar aquí: agregar /api
@CrossOrigin(origins = "http://localhost:4321")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    // Listar usuarios con el nombre del rol (JOIN)
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listar() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<Map<String, Object>> response = new ArrayList<>();

        for (Usuario u : usuarios) {
            Map<String, Object> userData = new HashMap<>();
            userData.put("id_usuario", u.getIdUsuario());  // ✅ Sin guión bajo
            userData.put("nombre_usuario", u.getNombreUsuario());
            userData.put("telefono", u.getTelefono());
            userData.put("email", u.getEmail());
            userData.put("id_rol", u.getIdRol());
            userData.put("clave", u.getClave());

            // JOIN con tabla rol
            Optional<Rol> rol = rolRepository.findById(u.getIdRol());
            userData.put("nombre_rol", rol.isPresent() ? rol.get().getNombre_rol() : null);

            response.add(userData);
        }

        return ResponseEntity.ok(response);
    }

    // Registrar nuevo usuario
    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody Map<String, Object> usuarioData) {
        try {
            Usuario usuario = new Usuario();
            usuario.setNombreUsuario((String) usuarioData.get("nombre_usuario"));
            usuario.setTelefono((String) usuarioData.get("telefono"));
            usuario.setEmail((String) usuarioData.get("email"));
            usuario.setClave((String) usuarioData.get("clave"));

            // Convertir id_rol a Integer
            Object idRolObj = usuarioData.get("id_rol");
            Integer idRol;
            if (idRolObj instanceof String) {
                idRol = Integer.parseInt((String) idRolObj);
            } else {
                idRol = (Integer) idRolObj;
            }
            usuario.setIdRol(idRol);

            Usuario saved = usuarioRepository.save(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {  // ✅ Cambiar a Long
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @RequestBody Usuario usuarioActualizado) {  // ✅ Cambiar a Long
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNombreUsuario(usuarioActualizado.getNombreUsuario());
            usuario.setTelefono(usuarioActualizado.getTelefono());
            usuario.setEmail(usuarioActualizado.getEmail());
            usuario.setClave(usuarioActualizado.getClave());
            usuario.setIdRol(usuarioActualizado.getIdRol());  // ✅ Cambiar a setIdRol
            return ResponseEntity.ok(usuarioRepository.save(usuario));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {  // ✅ Cambiar a Long
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ---------- LOGIN DE USUARIO ----------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String clave = credentials.get("clave");

        if (email == null || clave == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Faltan campos"));
        }

        // Buscar usuario por email
        Optional<Usuario> usuarioOpt = usuarioRepository.findAll().stream()
                .filter(u -> email.equals(u.getEmail()) && clave.equals(u.getClave()))
                .findFirst();

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Credenciales inválidas"));
        }

        Usuario usuario = usuarioOpt.get();

        // Obtener el rol
        Optional<Rol> rolOpt = rolRepository.findById(usuario.getIdRol());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("token", "fake-jwt-token-123456"); // luego lo puedes reemplazar por JWT real

        response.put("user", Map.of(
                "id_usuario", usuario.getIdUsuario(),
                "nombre_usuario", usuario.getNombreUsuario(),
                "telefono", usuario.getTelefono(),
                "email", usuario.getEmail(),
                "id_rol", usuario.getIdRol(),
                "nombre_rol", rolOpt.map(Rol::getNombre_rol).orElse("Desconocido")
        ));

        return ResponseEntity.ok(response);
    }

}